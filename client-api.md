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

+ Response 200 (text/plain)

  ok

+ Response 400 (application/json)

  [{"8bsqd8pasdufh3": "banner.jpg"}, 
   {"h9has77fasdhfo": "index.html"},
   {...}]



# Messages
Messages triggered by various system or user events.
For example sharing events.

## Receive all messages [GET]
A call to this resouce drains all messages.

+ Response 200 (application/json)

  [{"share": "iausdzfhase9"},
   {"share": "oasdhfasduwer7"}]
