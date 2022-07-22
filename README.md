xmlScan, 2013
=============

This application scans a bulk of xml files and retrieves needed information
using XPath expressions specified by user.

## Usage

To run this program you have to create a config file named
`xmlScan.properties` and set up needed values for properties.
There are no cli parameters.

Sample `xmlScan.properties` file:

```ini
xmldocs.source.folder = testXmlDocs/_test1

# if you want to search node in a namespace,
# please type xpath like ns_name:node_name or
# :node_name if node_name in a not named namespace.
target.xpath = :docID

# min threads number is 1
concurrent.threads.number = 3
```
