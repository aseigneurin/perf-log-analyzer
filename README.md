# Performance log analyzer

Simple tool that parses a log file and extracts the "longest" operations.

This is done by parsing the timestamp at the beginning of each line, computing the elapsed time between each couple of lines, and keeping the lines for which the elapsed time exceeds a threshold (default = 10 seconds).

## Example

Suppose you have the following file:

    2013-10-16 09:01:28,181 [10] INFO  Line A...
    2013-10-16 09:01:28,225 [10] INFO  Line B...
    2013-10-16 09:21:17,273 [10] DEBUG Line C...
    that wraps...
    on several lines.
    2013-10-16 09:21:28,228 [10] DEBUG Line D...
    2013-10-16 09:21:28,230 [10] DEBUG Line E...

The result will be:

    ### Took 1189 seconds to execute
        2013-10-16 09:01:28,225 [10] INFO  Line B...
    
    ### Took 10 seconds to execute
        2013-10-16 09:21:17,273 [10] DEBUG Line C...
        that wraps...
        on several lines.

## Building

Maven 3 is required.

Execute the following command:

    mvn package

The  `perf-log-analyzer.jar` file can then be located under the `target` directory.

## Usage

Here is the usage:

    Usage: ... [OPTION]... [FILE]
      -f, --format       Date-time format. Defaults to 'yyyy-MM-dd kk:mm:ss,S'.
      -t, --threshold    Threshold in seconds. Defaults to 10 seconds.

Example:

    java -jar target/perf-log-analyzer.jar mylogfile.txt