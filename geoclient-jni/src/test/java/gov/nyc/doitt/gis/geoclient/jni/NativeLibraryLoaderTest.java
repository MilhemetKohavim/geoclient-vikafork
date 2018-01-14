//
// This class is copied from Facebook's RocksDB v5.9.2
// See https://github.com/facebook/rocksdb
//

// Copyright (c) 2011-present, Facebook, Inc.  All rights reserved.
// This source code is licensed under the BSD-style license found in the
// LICENSE file in the root directory of this source tree. An additional grant
// of patent rights can be found in the PATENTS file in the same directory.
package gov.nyc.doitt.gis.geoclient.jni;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import gov.nyc.doitt.gis.geoclient.jni.util.Environment;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import static org.assertj.core.api.Assertions.assertThat;

public class NativeLibraryLoaderTest {

  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();

  @Test
  public void tempFolder() throws IOException {
    NativeLibraryLoader.getInstance().loadLibraryFromJarToTemp(
        temporaryFolder.getRoot().getAbsolutePath());
    final Path path = Paths.get(temporaryFolder.getRoot().getAbsolutePath(),
        Environment.getJniLibraryFileName("geoclient"));
    assertThat(Files.exists(path)).isTrue();
    assertThat(Files.isReadable(path)).isTrue();
  }

  @Test
  public void overridesExistingLibrary() throws IOException {
    File first = NativeLibraryLoader.getInstance().loadLibraryFromJarToTemp(
        temporaryFolder.getRoot().getAbsolutePath());
    NativeLibraryLoader.getInstance().loadLibraryFromJarToTemp(
        temporaryFolder.getRoot().getAbsolutePath());
    assertThat(first.exists()).isTrue();
  }
}
