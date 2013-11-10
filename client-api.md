# Geoffrey client HTTP API

Clients need to implement the complete API in order to integrate with the 
geoffrey-server.
The API is written in [apiblueprint](http://apiblueprint.org/) format.

# Files [/{client}/files]
The interval is up to the client.
The client name should be configurable by the user and must initially be derived
the the hostname as an attempt to minimize the name clash probability.


## Replace the old file list [PUT]
This resource is write only and intended as the endpoint for the client to inform
the server about the current file collection.
Writes to this endpoint will replace the old files list, hence all data need to
be send.

+ Request (application/json)

  [{"filename": "banner.jpg",
    "md5hash": "087asdhf97"}, 
   {...}]


+ Response 200 (text/plain)

  ok


# Messages [/{client}/messages]
Messages triggered by various system or user events.
For example sharing events.

## Receive all messages [GET]
A call to this resouce drains all messages.

+ Response 200 (application/json)

  [{"filename": "iausdzfhase9",
    "md5hash": "98aszdfhasd"},
   {...}]
