Step-sequence
=============

### Release

* Prepare a release `mvn release:clean release:prepare`
* In case something is broken: `mvn release:rollback`
* Do the release: `mvn release:perform`