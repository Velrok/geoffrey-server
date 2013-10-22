# API Requests

This lists all the vailable resources that build the API.
Parts of the URL that start with : mark variables or placeholders.

The `:client` is to be replaces by the client with its name, which might be
choosen at will.
The client name should be changeable by the user or extracted from the hostname,
since the API does not detect nor handle client name clashes.


## PUT /:client/files  

Update the file status.
The client has to send the **complete list of all available files**.
The list must be send as a JSON list of objects where each key is the md5-hash
of the file and the value is the filename.

Successfull requests will be answert with a http 200: OK.
Mal formed requests will be answert with a http 400, with the validation error
as the body in plain text.

**url**: `/:client/files`  
**body as JSON**: `[{"md5-hash": "filename"}, {...}]`  
responds:   
  200 (ok)  
  400 (the validateion error message)  


## GET /:client/messages

The client has to poll for new messages.
Messages are delivered as a JSON list of objects where each object has the event
type as the key and the playload (in JSON format) as the value.

All event types and their payload are explained in the next section.

**url**: `/:client/messages`  
**responds**:  
  200: JSON `[{"type": "json-payload"}, {...}]`  

### message types

This section list all message types receivable via
`GET /:client/messages`.

#### share
Contains only the md5 hash of the file that should be shared.

example:
`{"share" "7jasrr97hasd7"}`
