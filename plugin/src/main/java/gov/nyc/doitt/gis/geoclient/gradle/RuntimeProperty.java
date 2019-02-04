package gov.nyc.doitt.gis.geoclient.gradle;

import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.util.PatternFilterable;

public class RuntimeProperty {

    private final String name;
    private final Property<PropertySource> value;
    private final ListProperty<PropertySource> sources;
    private final Property<TestPolicy> testPolicy;

    /**
     * <p>
     * Central plugin abstraction representing "external" sources for runtime
     * configuration settings (generally Java System properties and environment
     * variables). The {@link GeoclientPlugin} creates extensions to hold named
     * instances of this class in Gradle's {@linkplain NamedDomainObjectContainer}.
     * </p>
     * <p>
     * Each instance contains a single {@code value} {@link PropertySource} member
     * representing the currently configured value. This member also serves as a way
     * to set a default value if the user does not provide custom
     * {@link PropertySource}s or if none of the provided sources can be resolved at
     * runtime.
     * </p>
     * <p>
     * This class also contains a {@link TestPolicy} which can be used
     * enable/disable the export of a resolved {@link RuntimeProperty} to test
     * executions. {@link TestPolicy} also implements the Gradle
     * {@linkplain PatternFilterable} interface allowing for a more granular,
     * pattern-based specification of which tests to configure. <b>NOTE:</b>
     * <p>
     * The defaults (i.e., Gradle <i>"conventions"</i>) for this class are set by
     * {@link AbstractRuntimePropertyExtension} due to Gradle lifecycle
     * requirements.
     * </p>
     *
     * @param name          unique name for an instance
     * @param objectFactory {@linkplain ObjectFactory} instance injected by Gradle
     *                      API
     */
    @javax.inject.Inject
    public RuntimeProperty(String name, ObjectFactory objectFactory) {
        super();
        this.name = name;
        this.value = objectFactory.property(PropertySource.class);
        this.sources = objectFactory.listProperty(PropertySource.class);
        this.testPolicy = objectFactory.property(TestPolicy.class);
    }

    public String getName() {
        return name;
    }

    public Property<PropertySource> getValue() {
        return value;
    }

    // Sets this.value but does not add it to this.sources
    public void setValue(PropertySource value) {
        this.value.set(value);
    }

    // Sets this.value and calls this.sources.empty() to initialize this.sources
    // with an empty list
    public void setConventions(PropertySource value) {
        this.value.convention(value);
        this.sources.empty();
    }

    // Adds this.value to this.sources and calls finalize on both properties
    // This should be called if none of the user-supplied PropertySources in
    // this.sources can be resolved
    public PropertySource finalizeWithCurrentValue() {
        if (!this.value.isPresent()) {
            throw new IllegalStateException(this + " 'value' is null");
        }
        if (!this.sources.isPresent()) {
            throw new IllegalStateException(this + " 'sources' is null");
        }
        this.value.finalizeValue();
        this.getSources().add(this.value);
        this.sources.finalizeValue();
        return this.value.get();
    }

    public PropertySource getDefaultValue() {
        if (this.value.isPresent()) {
            return this.value.get();
        }
        return null;
    }

    public ListProperty<PropertySource> getSources() {
        return sources;
    }

    public Property<TestPolicy> getTestPolicy() {
        return testPolicy;
    }

    public void setExportToTest(boolean export) {
        this.testPolicy.get().setExport(export);
    }

    @Override
    public String toString() {
        return "RuntimeProperty [name=" + name + ", value=" + value + ", sources=" + sources + "]";
    }
}