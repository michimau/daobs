# Associated resource indexer (experimental)

A metadata record may contain URL to remote resources (eg. PDF document, ZIP files).
This task will retrieve the content of such document using [Tika analysis toolkit](https://tika.apache.org/)
and index the content retrieved. This improve search results has the data related
to the metadata are also indexed.


Associated document URL are stored in the ```linkUrl``` field in the index.

