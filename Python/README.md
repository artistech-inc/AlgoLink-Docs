# AlgoLink Python Library Documentation

## Requirements

- Python 2.7
- `python-poster` module:

Ubuntu install of Python Poster:

```
sudo apt-get install python-poster
```

## Installation

## Description

A python script for accessing AlgoLink via web-services.

## Sending Messages

The `sent_entity_message()`, `set_queued_communication_result()`, and `set_queued_communication_result()` methods will post data to the web-server. This is the reason that the poster module is used. Any data that can be posted to a web-page can be passed along.

## Known Issues

The method signature have not been changed to support posting of any arbitrary data to the web-services.

If any problems occur using this library, please e-mail [Matt Aguirre](matta@artistech.com).

# Files

- [algolink.py](algolink.py)
- [algolink-ent.py](algolink-ent.py)
