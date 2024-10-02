# RedstoneBridge
A fabric mod to connect Minecraft redstone with real life.

# Setting up
You need some coding knowledge to set this up! The mod is originally designed for my rgb control software (which is not public at the moment), so you will need an external http server to use it.

## Request Structure
Every registered block update will send a http request to the specified url for the block. The request contains the name of the bridge, mode of the bidge, powered state, power level, correponding color (set in the config).

Example request for RGB mode:
```json
{
  "bridgeName": "example-rgb",
  "bridgeType": "RGB",
  "data": "#ff0000" 
}
```

Example request for DIGITAL mode:
```json
{
  "bridgeName": "example-digital",
  "bridgeType": "DIGITAL",
  "data": 1
}
```

Example request for ANALOGUE mode:
```json
{
"bridgeName": "example-analogue",
"bridgeType": "ANALOGUE",
"data": 15
}
```


## Example http server setup (with python and flask)
```py
# python 3.12
import flask

app = flask.Flask("example-server")

@app.route("/", methods=["POST"])
def bridge():
    request_json = flask.request.json
    bridge_name = request_json["bridgeName"]
    bridge_type = request_json["bridgeType"]
    bridge_data = request_json["data"]

    print(f"Recieved block update from {bridge_name}:")
    print(f"   Type: {bridge_type}")
    print(f"   Data: {bridge_data}")
    
    return "OK", 200

# Run the server on localhost:8080
app.run(host="127.0.0.1", port=8080)  # You should use app.run() only if you are running this on a closed network
```

### Known issues
 - Can be laggy