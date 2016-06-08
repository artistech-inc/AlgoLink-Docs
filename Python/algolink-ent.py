#!/usr/bin/env python
import urllib
import urllib2
import json
import time
from poster.encode import multipart_encode
from poster.streaminghttp import register_openers
register_openers()
#import optparse

#__DEFAULT_SERVER__ = u'http://localhost:8080/AlgoLink-Web/'
__DEFAULT_SERVER__ = u'http://localhost:8081/'
#__DEFAULT_SERVER__ = u'http://algolink.artistech.com/AlgoLink-Web/'

def test(server=__DEFAULT_SERVER__):
    """Test script for accessing and running a simulation over the Internet"""
    __BUILDER_TYPE__ = u'com.artistech.algolink.ent.DataBuilderConfig'

    a = AlgoLink(server)
    print a.get_db_config()
    print u'Server:        ' + a.get_server()
    print u'Version:       ' + a.get_version()
    print u'Is Logged In:  ' + str(a.is_loggedin())
    print u'Login:         ' + a.login()
    print u'Is Logged In:  ' + str(a.is_loggedin())
    print u'ID:            ' + a.get_id()
    
    db_cfg = a.get_config_schema(__BUILDER_TYPE__)
    db_cfg["duration"] = 1440
    #db_cfg["duration"] = 1440 * 28
    print 'duration', db_cfg["duration"]

    # a communication filter is a way to hook into AlgoLink and change
    # how a communication is allowed to continue.  Sometimes this can be a
    # do not allow if the sender is already busy.  The default is a 'pass-through'
    # where the communication is always successful.  When using the WebComFilter shown
    # below, the 'get_queued_communications' function will return back any communications
    # that are waiting for "yes or no".  A 'no' is implicit in not receiving a response.
    db_cfg["commFilter"] = 'com.artistech.algolink.web.commfilters.WebCommFilter'
    org_list = [{__BUILDER_TYPE__: db_cfg}]

    print u'Create Population:'
    print a.create_population(org_list)

    complete = False
    sleepTime = 0
    try:
        while not complete:
            # print 'sleepTime: ', sleepTime
            time.sleep(sleepTime)
            sleepTime = 0
            t = a.tick()
            complete = t['complete']
            if not complete:
                print t['time']
                f = a.get_queued_communications()
                if not f is None:
                    # sleepTime = 0.02 * len(f)
                    # if sleepTime > 0.5:
                    #     sleepTime = 0.5
                    # for x in f:
                    #     print 'received:', x['id']
                    #     res = a.set_queued_communication_result(x['id'], 'SENT THROUGH PYTHON!')
                    #     print 'responded to:', res

                    toSend = {}
                    res = []
                    toSend['results'] = res

                    for x in f:
                        r = {}
                        r['commId'] = x['id']
                        r['result'] = 'SENT THROUGH PYTHON!'
                        # print 'SENT THROUGH PYTHON!'
                        res.append(r)
                        # a.set_queued_communication_result(r['commId'], r['result'])
                    a.set_queued_communication_result_2(toSend)
                if t['errorMessage'] != "":
                    print t['errorMessage']
                    complete = True
    finally:
        print u'Logout:        ' + str(a.logout())
        print u'Is Logged In:  ' + str(a.is_loggedin())

class AlgoLink:
    def __init__(self, server=__DEFAULT_SERVER__):
        """Constructor, takes an optional argument specifying the location of the AlgoLing server"""
        self.__id = u''
        self.__server = server

    def get_server(self):
        """Return the current AlgoLink server location"""
        return self.__server

    def logout(self):
        """Logout of the server"""
        ret = self.is_loggedin()
        if self.is_loggedin():
            values = {u'algolink_id' : self.__id}
            data = urllib.urlencode(values)
            req = urllib2.Request(self.__server + u'logout.json', data)
            f = urllib2.urlopen(req)
            self.__id = u''
        return ret

    def get_id(self):
        """Get the current algolink_id value"""
        return self.__id
 
    def login(self):
        """Login to the AlgoLink server and obtain an algolink_id"""
        if not self.is_loggedin():
            req = urllib2.Request(self.__server + u'login.json')
            f = urllib2.urlopen(req)
            result = f.read()
            self.__id = json.loads(result)
        return self.__id

    def is_loggedin(self):
        """Returns if the object is currently logged into the AlgoLink service"""
        return self.__id != u'' 

    def get_version(self):
        """Returns the version of Algolink running on the server"""
        req = urllib2.Request(self.__server + u'version.json')
        f = urllib2.urlopen(req)
        result = f.read()
        return json.loads(result)

    def get_config_schema(self, t):
        """Returns a object which represents the coniguration for the specified type"""
        values = {u'type' : t}
        data = urllib.urlencode(values)
        req = urllib2.Request(self.__server + u'configSchema.json', data)
        f = urllib2.urlopen(req)
        result = f.read()
        return json.loads(result)

    # def get_all_config_types(self):
    #     """Returns a array of types which can be configured"""
    #     req = urllib2.Request(self.__server + u'allConfigTypes.json')
    #     f = urllib2.urlopen(req)
    #     result = f.read()
    #     return json.loads(result)

    def get_organization_types(self):
        """Returns an array of organizations available to AlgoLink"""
        req = urllib2.Request(self.__server + u'organizationTypes.json')
        f = urllib2.urlopen(req)
        result = f.read()
        return json.loads(result)

    # def get_data_builders(self):
    #     """Returns an array of data builders available to AlgoLink"""
    #     req = urllib2.Request(self.__server + u'dataBuilders.json')
    #     f = urllib2.urlopen(req)
    #     result = f.read()
    #     return json.loads(result)

    def get_communication_filters(self):
        """Returns an array of communication filters available to AlgoLink"""
        req = urllib2.Request(self.__server + u'communicationFilters.json')
        f = urllib2.urlopen(req)
        result = f.read()
        return json.loads(result)

    def get_entities(self):
        """Returns an array of entity ID values in the current simulation"""
        if self.is_loggedin():
            values = {u'algolink_id' : self.__id}
            data = urllib.urlencode(values)
            req = urllib2.Request(self.__server + u'entities.json', data)
            f = urllib2.urlopen(req)
            result = f.read()
            return json.loads(result)
        return None

    # def get_edge_entities(self):
    #     """Returns an array of entity ID values which are in the EDGE organization"""
    #     if self.is_loggedin():
    #         values = {u'algolink_id' : self.__id}
    #         data = urllib.urlencode(values)
    #         req = urllib2.Request(self.__server + u'edgeEntities.json', data)
    #         f = urllib2.urlopen(req)
    #         result = f.read()
    #         return json.loads(result)
    #     return None

    # def get_ummf_entities(self):
    #     """Returns an array of entity ID values which are in the UMMF organization"""
    #     if self.is_loggedin():
    #         values = {u'algolink_id' : self.__id}
    #         data = urllib.urlencode(values)
    #         req = urllib2.Request(self.__server + u'ummfEntities.json', data)
    #         f = urllib2.urlopen(req)
    #         result = f.read()
    #         return json.loads(result)
    #     return None

    def get_communications(self):
        """Returns an array of communication objects which have accumulated since the last "get_communications()" request"""
        if self.is_loggedin():
            values = {u'algolink_id' : self.__id}
            data = urllib.urlencode(values)
            req = urllib2.Request(self.__server + 'getCommunications.json', data)
            f = urllib2.urlopen(req)
            result = f.read()
            return json.loads(result)
        return None

    def get_positions(self):
        """Returns an array of position objects for each entity ID value"""
        if self.is_loggedin():
            values = {u'algolink_id' : self.__id}
            data = urllib.urlencode(values)
            req = urllib2.Request(self.__server + u'getPositions.json', data)
            f = urllib2.urlopen(req)
            result = f.read()
            return json.loads(result)
        return None

    def get_db_config(self):
        """Get the current data builder config"""
        values = {}
        if self.is_loggedin():
            values['algolink_id'] = self.__id
        data = urllib.urlencode(values)
        req = urllib2.Request(self.__server + u'getDBConfig.json', data)
        f = urllib2.urlopen(req)
        result = f.read()
        return json.loads(result)

    def get_population(self):
        """Get information on the current population"""
        if self.is_loggedin():
            values = {u'algolink_id' : self.__id}
            data = urllib.urlencode(values)
            req = urllib2.Request(self.__server + 'getPopulation.json', data)
            f = urllib2.urlopen(req)
            result = f.read()
            return json.loads(result)
        return None
        
    def send_entity_message(self, entity, message):
        if self.is_loggedin():
            values = {u'algolink_id' : self.__id,
                      u'id': entity,
                      u'message': message}
            datagen, headers = multipart_encode(values)
            # data = urllib.urlencode(values)
            req = urllib2.Request(self.__server + u'send_entity_message.json', datagen, headers)
            # req.add_data(urllib.urlencode(values));
            f = urllib2.urlopen(req)
            result = f.read()
            return json.loads(result)
        return None

    def tick(self):
        """Tell the simulation to tick one "minute" and return communications and other pertinent information"""
        if self.is_loggedin():
            values = {u'algolink_id' : self.__id}
            data = urllib.urlencode(values)
            req = urllib2.Request(self.__server + u'tick.json', data)
            f = urllib2.urlopen(req)
            result = f.read()
            return json.loads(result)
        return None

    def create_population(self, configs, enable_tick_support=True):
        """Create and start the simulation with the specified configurations, the simulation is started in such a way that tick() is used to progress the simulation and receive information on the execution"""
        if self.is_loggedin():
            values = {u'algolink_id' : self.__id,
                      u'enable_tick_support': enable_tick_support,
                      u'configs' : json.dumps(configs) }
            data = urllib.urlencode(values)     
            req = urllib2.Request(self.__server + u'createPopulation.json', data)
            f = urllib2.urlopen(req)
            result = f.read()
            return json.loads(result)
        return None

    def get_queued_communications(self):
        """Get any communications that have been queued for checking to see if the communicaiton is a success betwen 2 entities"""
        if self.is_loggedin():
            values = {u'algolink_id' : self.__id}
            data = urllib.urlencode(values)
            req = urllib2.Request(self.__server + u'GetQueuedWebComms', data)
            f = urllib2.urlopen(req)
            result = f.read()
            return json.loads(result)
        return None

    def set_queued_communication_result_2(self, results):
        """Set that a communication was successful between two entities"""
        if self.is_loggedin() and len(results['results']) > 0:
            values = {u'results': json.dumps(results)}
            datagen, headers = multipart_encode(values)
            req = urllib2.Request(self.__server + u'SetQueuedWebCommunicationResult', datagen, headers)
            # req.add_data(urllib.urlencode(values));
            f = urllib2.urlopen(req)
            result = f.read()
            return json.loads(result)
        return None

    def set_queued_communication_result(self, comm_id, result, payload={}):
        """Set that a communication was successful between two entities"""
        if self.is_loggedin():
            values = {u'commId': comm_id,
                      u'result': result}
            for key in payload.keys():
                values[key] = payload[key]
            datagen, headers = multipart_encode(values)
            req = urllib2.Request(self.__server + u'SetQueuedWebCommunicationResult', datagen, headers)
            # req.add_data(urllib.urlencode(values));
            f = urllib2.urlopen(req)
            result = f.read()
            return json.loads(result)
        return None

    def set_communication_started(self, comm_id, result=''):
        """Set that a communication was successfully started between two entities"""
        if self.is_loggedin():
            values = {u'commId': comm_id,
                      u'result': result}
            datagen, headers = multipart_encode(values)
            req = urllib2.Request(self.__server + u'StartQueuedCommunication', datagen, headers)
            # req.add_data(urllib.urlencode(values));
            f = urllib2.urlopen(req)
            result = f.read()
            return json.loads(result)
        return None

    def set_communication_cancelled(self, comm_id, result=''):
        """Set that a communication was cancelled between two entities"""
        if self.is_loggedin():
            values = {u'commId': comm_id,
                      u'result': result}
            datagen, headers = multipart_encode(values)
            req = urllib2.Request(self.__server + u'CancelQueuedCommunication', datagen, headers)
            # req.add_data(urllib.urlencode(values));
            f = urllib2.urlopen(req)
            result = f.read()
            return json.loads(result)
        return None

    def set_communication_received(self, comm_id, result, payload={}):
        """Set that a communication was successful between two entities"""
        if self.is_loggedin():
            values = {u'commId': comm_id,
                      u'result': result}
            for key in payload.keys():
                values[key] = payload[key]
            datagen, headers = multipart_encode(values)
            req = urllib2.Request(self.__server + u'ReceivedQueuedCommunication', datagen, headers)
            # req.add_data(urllib.urlencode(values));
            f = urllib2.urlopen(req)
            result = f.read()
            return json.loads(result)
        return None

if __name__ == '__main__':
    #op = optparse.OptionParser(version='[%prog] ' + AlgoLink().get_version())
    #(options, args) = op.parse_args()
    import sys
    if (len(sys.argv) > 1):
        test(sys.argv[1])
    else:
        test()
