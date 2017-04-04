import com.google.copybara.testing.DummyRevision;
  private boolean skipPush;
    destinationFiles = Glob.createGlob(ImmutableList.of("**"));
            + "    skip_push = %s,\n"
            + ")", url, fetch, push, skipPush ? "True" : "False"));
  private void process(Destination.Writer writer, DummyRevision originRef)
  private void processWithBaseline(Destination.Writer writer, DummyRevision originRef,
      DummyRevision originRef,
        new DummyRevision("origin_ref"));
    processWithBaselineAndConfirmation(
        destinationFirstCommit().newWriter(destinationFiles),
        new DummyRevision("origin_ref"),
        /*baseline=*/ null, /*askForConfirmation=*/
        true);
        new DummyRevision("origin_ref1"),
        /*baseline=*/ null, /*askForConfirmation=*/
        true);
        new DummyRevision("origin_ref2"),
        /*baseline=*/ null, /*askForConfirmation=*/
        true);
    processWithBaselineAndConfirmation(
        destinationFirstCommit().newWriter(destinationFiles),
        new DummyRevision("origin_ref"),
        /*baseline=*/ null, /*askForConfirmation=*/
        true);
    DummyRevision ref = new DummyRevision("origin_ref");
    destinationFiles = Glob.createGlob(ImmutableList.of("**"), ImmutableList.of("excluded"));
    process(
        destination().newWriter(destinationFiles),
        new DummyRevision("origin_ref"));
    process(
        destination().newWriter(destinationFiles),
        new DummyRevision("origin_ref"));
    process(
        destinationFirstCommit().newWriter(destinationFiles),
        new DummyRevision("origin_ref"));
    process(
        destination().newWriter(destinationFiles),
        new DummyRevision("origin_ref"));
    destinationFiles = Glob.createGlob(ImmutableList.of("foo/**"));
    process(
        destination().newWriter(destinationFiles),
        new DummyRevision("origin_ref"));
    DummyRevision ref1 = new DummyRevision("first");
    Glob firstGlob = Glob.createGlob(ImmutableList.of("foo/**", "bar/**"));
    DummyRevision ref2 = new DummyRevision("second");
    Writer writer2 = destination().newWriter(Glob.createGlob(ImmutableList.of("baz/**")));
    assertThat(destination().newWriter(firstGlob)
                   .getPreviousRef(ref1.getLabelName())).isEqualTo(
    Destination.Writer writer =
        destinationFirstCommit().newWriter(destinationFiles);
    process(writer, new DummyRevision("first_commit"));
    process(writer, new DummyRevision("second_commit"));
    process(writer, new DummyRevision("third_commit"));
    process(
        destinationFirstCommit().newWriter(destinationFiles),
        new DummyRevision("first_commit"));
    assertThat(
            destination()
                .newWriter(destinationFiles)
                .getPreviousRef(DummyOrigin.LABEL_NAME))
    process(
        destinationFirstCommit().newWriter(destinationFiles),
        new DummyRevision("first_commit"));
    destination()
        .newWriter(destinationFiles)
        .getPreviousRef(DummyOrigin.LABEL_NAME);
    process(
        destinationFirstCommit().newWriter(destinationFiles),
        new DummyRevision("first_commit").withTimestamp(Instant.ofEpochSecond(1414141414)));
    process(
        destination().newWriter(destinationFiles),
        new DummyRevision("second_commit").withTimestamp(Instant.ofEpochSecond(1515151515)));
    process(
        destinationFirstCommit().newWriter(destinationFiles),
        new DummyRevision("first_commit"));
    process(
        destinationFirstCommit().newWriter(destinationFiles),
        new DummyRevision("first_commit").withTimestamp(Instant.ofEpochSecond(1414141414)));
    process(
        destination().newWriter(destinationFiles),
        new DummyRevision("second_commit").withTimestamp(Instant.ofEpochSecond(1414141490)));
    process(
        destinationFirstCommit().newWriter(destinationFiles),
        new DummyRevision("first_commit").withTimestamp(Instant.ofEpochSecond(1414141414)));
    process(
        destination().newWriter(destinationFiles),
        new DummyRevision("second_commit").withTimestamp(Instant.ofEpochSecond(1414141490)));
    process(
        destinationFirstCommit().newWriter(destinationFiles),
        new DummyRevision("first_commit"));
    process(
        destinationFirstCommit().newWriter(destinationFiles),
        new DummyRevision("first_commit"));
    DummyRevision firstCommit = new DummyRevision("first_commit")
    process(
        destinationFirstCommit().newWriter(destinationFiles),
        firstCommit);
    destinationFiles = Glob.createGlob(ImmutableList.of("**"), ImmutableList.of("excluded.txt"));
    process(
        destination().newWriter(destinationFiles),
        new DummyRevision("ref"));
    destinationFiles = Glob.createGlob(ImmutableList.of("**"), ImmutableList.of("**/HEAD"));
    process(
        destination().newWriter(destinationFiles),
        new DummyRevision("ref"));
    DummyRevision ref = new DummyRevision("origin_ref");
    destinationFiles = Glob.createGlob(ImmutableList.of("**"), ImmutableList.of("excluded"));
    processWithBaseline(
        destination().newWriter(destinationFiles), ref, firstCommit);
    DummyRevision ref = new DummyRevision("origin_ref");
    processWithBaseline(
        destination().newWriter(destinationFiles), ref, firstCommit);
    DummyRevision ref = new DummyRevision("origin_ref");
    processWithBaseline(
        destination().newWriter(destinationFiles), ref, firstCommit);
    Destination.Writer writer =
        destinationFirstCommit().newWriter(destinationFiles);
        writer.write(TransformResults.of(workdir, new DummyRevision("ref1")), console);
    result = writer.write(TransformResults.of(workdir, new DummyRevision("ref2")), console);
    DummyRevision ref = new DummyRevision("origin_ref");
    destinationFiles = Glob.createGlob(ImmutableList.of("**"), ImmutableList.of(".gitignore"));
    DummyRevision ref = new DummyRevision("origin_ref");
  @Test
  public void testLocalRepo() throws Exception {
    checkLocalRepo(false);

    GitTesting.assertThatCheckout(repo(), "master")
        .containsFile("test.txt", "another content")
        .containsNoMoreFiles();
  }

  @Test
  public void testLocalRepoSkipPush() throws Exception {
    skipPush = true;
    GitRepository localRepo = checkLocalRepo(false);

    GitTesting.assertThatCheckout(repo(), "master")
        .containsFile("foo", "foo\n")
        .containsNoMoreFiles();

    // A simple push without origin is able to update the correct destination reference
    localRepo.simpleCommand("push");

    GitTesting.assertThatCheckout(repo(), "master")
        .containsFile("test.txt", "another content")
        .containsNoMoreFiles();
  }

  @Test
  public void testLocalRepoSkipPushFlag() throws Exception {
    GitRepository localRepo = checkLocalRepo(true);

    GitTesting.assertThatCheckout(repo(), "master")
        .containsFile("foo", "foo\n")
        .containsNoMoreFiles();

    // A simple push without origin is able to update the correct destination reference
    localRepo.simpleCommand("push");

    GitTesting.assertThatCheckout(repo(), "master")
        .containsFile("test.txt", "another content")
        .containsNoMoreFiles();
  }

  private GitRepository checkLocalRepo(boolean skipPushFlag)
      throws Exception {
    fetch = "master";
    push = "master";

    Files.write(workdir.resolve("test.txt"), "some content".getBytes());

    Path scratchTree = Files.createTempDirectory("GitDestinationTest-testLocalRepo");
    Files.write(scratchTree.resolve("foo"), "foo\n".getBytes(UTF_8));
    repo().withWorkTree(scratchTree).add().force().files("foo").run();
    repo().withWorkTree(scratchTree).simpleCommand("commit", "-a", "-m", "change");

    options.gitDestination.skipPush = skipPushFlag;
    Path localPath = Files.createTempDirectory("local_repo");

    options.gitDestination.localRepoPath = localPath.toString();
    Writer writer = destination().newWriter(destinationFiles);
    process(writer, new DummyRevision("origin_ref1"));

    //    Path localPath = Files.createTempDirectory("local_repo");
    GitRepository localRepo = GitRepository.initScratchRepo(/*verbose=*/true, localPath,
        System.getenv());

    GitTesting.assertThatCheckout(localRepo, "master")
        .containsFile("test.txt", "some content")
        .containsNoMoreFiles();

    Files.write(workdir.resolve("test.txt"), "another content".getBytes());
    process(writer, new DummyRevision("origin_ref2"));

    GitTesting.assertThatCheckout(localRepo, "master")
        .containsFile("test.txt", "another content")
        .containsNoMoreFiles();

    String changes = localRepo.simpleCommand("log", "--no-color", "--format=%B---").getStdout();
    assertThat(changes).isEqualTo("test summary\n"
        + "\n"
        + "DummyOrigin-RevId: origin_ref2\n"
        + "---\n"
        + "test summary\n"
        + "\n"
        + "DummyOrigin-RevId: origin_ref1\n"
        + "---\n"
        + "change\n"
        + "---\n");
    return localRepo;
  }

    DummyRevision ref1 = new DummyRevision("origin_ref1");
    DummyRevision ref2 = new DummyRevision("origin_ref2");
    process(
        destinationFirstCommit().newWriter(destinationFiles), ref1);