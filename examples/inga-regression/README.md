INGA regression tests
===

Contiki-INGA comes with a test suite located under `/tools/profiling/test` that can be used to perform hardware regression tests on real INGA nodes.

Execution of all regression tests:

    ./tools/profiling/test/test.py -s examples/inga-regression/suite_config.yaml -n examples/inga-regression/node_config.yaml -t examples/inga-regression/sensor-tests/test_config.yaml

Test Folders
---

Each subfolder contains collection of tests for a specific domain:

* `coffee-tests`
  
  Tests for the COFFEE file system driver


* `fat-tests`

  Tests for the FAT file system driver

* `net-tests`

  Tests for network stacks like RIME, UDP, TCP

* `sensor-tests`

  Tests for the INGA sensor drivers

* `timer-tests`

  Tests for contiki timers (etimer, rtimer, ...)


Naming convention
---
Testfolders are named `CATEGORY-tests` where CATEGORY is one of
`timer`, `sensor`, `net`, `fat`, `coffee`, ...

Testfiles are named `NAME-test` where NAME is the name of the test.

Tests in testfolders are named `CATEGORY-NAME`.
