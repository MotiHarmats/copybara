/*
 * Copyright (C) 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.copybara;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.copybara.util.TempDirectoryFactory;
import com.google.copybara.util.console.Console;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.Map;
import javax.annotation.Nullable;

/**
 * General options available for all the program classes.
 */
public final class GeneralOptions implements Option {

  public static final String NOANSI = "--noansi";
  public static final String FORCE = "--force";
  public static final String CONFIG_ROOT_FLAG = "--config-root";
  public static final String CONFIG_ORIGIN_LOCATION_FLAG = "--config-origin-location";

  private final Map<String, String> environment;
  private final FileSystem fileSystem;
  private final boolean verbose;
  private final Console console;
  private final boolean disableReversibleCheck;
  private final boolean force;
  @Nullable
  private final Path configRoot;
  @Nullable
  private final Path outputRoot;
  @Nullable
  private final String configOriginLocation;

  @VisibleForTesting
  public GeneralOptions(FileSystem fileSystem, boolean verbose, Console console) {
    this(System.getenv(), fileSystem, verbose, console, /*configRoot=*/null, /*outputRoot=*/null,
        /*configOriginLocation=*/null, /*disableReversibleCheck=*/false, /*force=*/false);
  }

  @VisibleForTesting
  public GeneralOptions(
      Map<String, String> environment, FileSystem fileSystem, boolean verbose, Console console) {
    this(environment, fileSystem, verbose, console, /*configRoot=*/null, /*outputRoot=*/null,
        /*configOriginLocation=*/null, /*disableReversibleCheck=*/false, /*force=*/false);
  }

  @VisibleForTesting
  public GeneralOptions(Map<String, String> environment, FileSystem fileSystem, boolean verbose,
      Console console, @Nullable Path configRoot, @Nullable Path outputRoot,
      @Nullable String configOriginLocation, boolean disableReversibleCheck, boolean force)
  {
    this.environment = ImmutableMap.copyOf(Preconditions.checkNotNull(environment));
    this.console = Preconditions.checkNotNull(console);
    this.fileSystem = Preconditions.checkNotNull(fileSystem);
    this.verbose = verbose;
    this.configRoot = configRoot;
    this.outputRoot = outputRoot;
    this.configOriginLocation = configOriginLocation;
    this.disableReversibleCheck = disableReversibleCheck;
    this.force = force;
  }

  public Map<String, String> getEnvironment() {
    return environment;
  }

  public boolean isVerbose() {
    return verbose;
  }

  public Console console() {
    return console;
  }

  public FileSystem getFileSystem() {
    return fileSystem;
  }

  public boolean isDisableReversibleCheck() {
    return disableReversibleCheck;
  }

  public boolean isForced() {
    return force;
  }

  /**
   * Returns current working directory
   */
  public Path getCwd() {
    return fileSystem.getPath(environment.get("PWD"));
  }

  @Nullable
  public Path getConfigRoot() {
    return configRoot;
  }

  @Nullable
  public Path getOutputRoot() {
    return outputRoot;
  }

  @Nullable
  public String getConfigOriginLocation() {
    return configOriginLocation;
  }

  /**
   * Returns a {@link TempDirectoryFactory} capable of creating temporary directories.
   *
   * <p>By default, the temporary directories are created under {@code $HOME/copybara/out}, but
   * it can be overridden with the flag --output-root.
   */
  public TempDirectoryFactory getTmpDirectoryFactory() {
    Path rootPath = outputRoot != null
        ? outputRoot
        : fileSystem.getPath(environment.get("HOME")).resolve("copybara/out/");
    return new TempDirectoryFactory(rootPath);
  }

  @Parameters(separators = "=")
  public static final class Args {
    @Parameter(names = "-v", description = "Verbose output.")
    boolean verbose;

    // We don't use JCommander for parsing this flag but we do it manually since
    // the parsing could fail and we need to report errors using one console
    @SuppressWarnings("unused")
    @Parameter(names = NOANSI, description = "Don't use ANSI output for messages")
    boolean noansi = false;

    @Parameter(names = FORCE, description = "Force the migration even if Copybara ca  nnot find in"
        + " the destination a change that is an ancestor of the one(s) being migrated. This should"
        + " be used with care, as it could lose changes when migrating a previous/conflicting"
        + " change.")
    boolean force = false;

    @Parameter(names = CONFIG_ROOT_FLAG,
        description = "Configuration root path to be used for resolving absolute config labels"
            + " like '//foo/bar'")
    String configRoot;

    // TODO(copybara-team): Set hidden=false
    @Parameter(names = CONFIG_ORIGIN_LOCATION_FLAG, hidden = true,
        description = "If set, for each imported change Copybara will load the "
            + "configuration from the origin change using the given location. For iterative "
            + "workflows, each change will be imported loading the config from the given change. "
            + "For squash workflows, the config will be read from the last change part of the "
            + "squash. Defaults to null.")
    String configOriginLocation;

    @Parameter(names = "--disable-reversible-check",
        description = "If set, all workflows will be executed without reversible_check, overriding"
            + " the  workflow config and the normal behavior for CHANGE_REQUEST mode.")
    boolean disableReversibleCheck = false;

    @Parameter(names = "--output-root",
            description = "The root directory where to generate output files. "
                + "If not set, ~/copybara/out is used by default.")
    String outputRoot = null;

    /**
     * This method should be called after the options have been set but before are used by any class.
     */
    public GeneralOptions init(
        Map<String, String> environment, FileSystem fileSystem, Console console)
        throws IOException {
      Path configRoot = this.configRoot != null ? fileSystem.getPath(this.configRoot) : null;
      Path outputRoot = this.outputRoot != null ? fileSystem.getPath(this.outputRoot) : null;
      return new GeneralOptions(
          environment, fileSystem, verbose, console, configRoot, outputRoot, configOriginLocation,
          disableReversibleCheck, force);
    }
  }
}
