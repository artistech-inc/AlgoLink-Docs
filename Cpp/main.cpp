#include <stdio.h>
#include <json/json.h>
#include <iostream>
#include <list>
#include <boost/program_options.hpp>

#include "algolink.h"

namespace po = boost::program_options;
using namespace std;
using namespace artistech;

// this is a wrapper function around json_object_object_get_ex since
// json_object_object_get was deprecated and generating warnings
// under Ubuntu 16.04
json_object* get(json_object* rootObj, const char* key)
{
  json_object* returnObj;
  if (json_object_object_get_ex(rootObj, key, &returnObj))
  {
    return returnObj;
  }
  return NULL;
}

// much of this code is out of date since this was the method used when
// using the C# implementation of AlgoLink.  The C++ library (algolink.*)
// has been updated, but this code has not.
// Please view the algolink-java.py file for an example of using alogolink
// via an external library.
// -- Matt Aguirre 7/30/14
int main(int argc, char** argv) {
  int duration = 1440;
  string default_server = "http://localhost:8084/AlgoLink/";

  po::options_description desc("Allowed options");
  desc.add_options()
    ("help", "Show this message.")
    ("duration", po::value<int>(&duration)->default_value(duration), "Simulation Duration")
    ("server", po::value<string>(&default_server)->default_value(default_server), "AlgoLink Server")
  ;

  po::variables_map vm;
  po::store(po::parse_command_line(argc, argv, desc), vm);
  po::notify(vm);

  if (vm.count("help")) {
    cout << desc << endl;
    return 1;
  }

  if (vm.count("server")) {
    default_server = vm["server"].as<string>();
  }

  /*AlgoLink Wrapper Object*/
  AlgoLink* m_algolink = new AlgoLink(default_server);

  /*Default AlgoLink Types*/
  string __BUILDER_TYPE__ = "com.artistech.algolink.core.DataBuilderConfig";
  //string __TEST_TYPE__ = "AlgoLink.Core.Scenario1.Scenario1Organization";
  string __TEST_TYPE__ = "com.artistech.algolink.orgs.scenario1.Scenario1Config";

  /*Login (and test login functionality)*/
  printf("get_server(): %s\n", m_algolink->get_server().c_str());
  printf("get_version(): %s\n", m_algolink->get_version().c_str());
#ifdef DEBUG
  printf("is_loggedin(): %s\n", m_algolink->is_loggedin() ? "true": "false");
#endif
  printf("login(): %s\n", m_algolink->login().c_str());
#ifdef DEBUG
  printf("is_loggedin(): %s\n", m_algolink->is_loggedin() ? "true": "false");
  printf("login(): %s\n", m_algolink->login().c_str());
#endif
  /*Create the scenario:*/
  json_object* org_cfg = m_algolink->get_config_schema(__TEST_TYPE__);
  json_object* db_cfg = m_algolink->get_config_schema(__BUILDER_TYPE__);

  /*Change the size of the org...*/
  /*By Default, this value is 0 and each organization is in charge of creating
    it's own population, here, we can say, create 20 entities that are not
    associated with any organization.  This is required because the "noise" organization
    does not create any entities in the population. */
  json_object_object_del(db_cfg, "neutralPopulation");
  json_object_object_add(db_cfg, "neutralPopulation", json_object_new_int(0));

  /*Change the duration of the dimulation: default is to run for 4 weeks, set to run for 1 day.*/
  json_object_object_del(db_cfg, "duration");
  json_object_object_add(db_cfg, "duration", json_object_new_int(duration));

  // /*Change the default max communications/minute rate to 10, default is 0*/
  // json_object_object_del(db_cfg, "maxCommPerMinute");
  // json_object_object_add(db_cfg, "maxCommPerMinute", json_object_new_int(0));

  // /*Change the default min communications/minute rate to 5, default is 0*/
  // json_object_object_del(db_cfg, "minCommPerMinute");
  // json_object_object_add(db_cfg, "minCommPerMinute", json_object_new_int(0));

  /*Change the communication filter*/
  json_object_object_del(db_cfg, "commFilter");
  json_object_object_add(db_cfg, "commFilter", json_object_new_string("com.artistech.algolink.web.commfilters.WebCommFilter"));

  /*Change the Real-Time-Factor: 1000 == RealTime, 0=as-fast-as-possible*/
  json_object_object_del(db_cfg, "realTimeFactor");
  json_object_object_add(db_cfg, "realTimeFactor", json_object_new_int(0));

  /*This setting will tell AlgoLink to give all entities a position:*/
  json_object_object_del(db_cfg, "moveAllEntities");
  json_object_object_add(db_cfg, "moveAllEntities", json_object_new_boolean(false));

  json_object* config = json_object_new_array();
  json_object* obj1 = json_object_new_object();
  json_object* obj2 = json_object_new_object();
  json_object_object_add(obj1, __BUILDER_TYPE__.c_str(), db_cfg);
  json_object_object_add(obj2, __TEST_TYPE__.c_str(), org_cfg);

  json_object_array_add(config, obj1);
  json_object_array_add(config, obj2);

#ifdef DEBUG
  json_object* all_org_typoes = m_algolink->get_organization_types();
  json_object* all_data_builders = m_algolink->get_data_builders();
  json_object* all_config_types = m_algolink->get_all_config_types();

  printf("get_organization_types()\n%s\n", json_object_to_json_string(all_org_typoes));
  printf("get_data_builders()\n%s\n", json_object_to_json_string(all_data_builders));
  printf("get_all_config_types():\n%s\n", json_object_to_json_string(all_config_types));
#endif

  printf("get_config_schema(\"%s\")\n%s\n", __TEST_TYPE__.c_str(), json_object_to_json_string(org_cfg));
  printf("get_config_schema(\"%s\")\n%s\n", __BUILDER_TYPE__.c_str(), json_object_to_json_string(db_cfg));
  
  /* Send FALSE here to let the simulation run on the server w/o requiring the "tick()" poll to push the simulation along. */
  json_object* build = m_algolink->create_population(config, true);
  printf("create_population(): %s\n", json_object_to_json_string(build));
  
  // /* Get arrays of entities that are specific to the diversity caching simulation: */    
  // list<string> cache_entities = m_algolink->get_diversity_caches();
  // list<string> non_cache_entities = m_algolink->get_diversity_entities();
  
  // for(list<string>::iterator it = cache_entities.begin(); it != cache_entities.end(); it++) {
  //   printf("Cache Entity: %s\n", (*it).c_str());
  //   m_algolink->send_entity_message(*it, "{time, src, dest, size, lifetime, x, y, group}");
  // }
  // for(list<string>::iterator it = non_cache_entities.begin(); it != non_cache_entities.end(); it++) {
  //   printf("Non-Cache Entity: %s\n", (*it).c_str());
  //   m_algolink->send_entity_message(*it, "{time, src, dest, size, lifetime, x, y, group}");
  // }

  /* Cleanup Memory */
  json_object_put(build);
  json_object_put(org_cfg);
  json_object_put(db_cfg);
  json_object_put(config);
  json_object_put(obj1);
  json_object_put(obj2);

#ifdef DEBUG  
  json_object_put(all_org_typoes);
  json_object_put(all_data_builders);
  json_object_put(all_config_types);
#endif
  bool complete = false;
  printf("Will Now Execute for %d ticks\n", duration);
  while (!complete) {
    /*Check Out The Current Communicaitons*/
    json_object* tick = m_algolink->tick();
    complete = json_object_get_boolean(get(tick, "complete"));
    //json_object* time = json_object_object_get(tick, "time");
    //count++;
    if (!complete) {
      json_object* comms = m_algolink->get_queued_communications();
      int count = json_object_array_length(comms);

      if (count > 0) {
        //printf("tick(): %s\n", json_object_get_string(time));
        printf("Queued Communications: %d\n", count);
        for(int ii = 0; ii < count; ii++) {
#ifdef DEBUG
          printf("\tRespond to Queued Communications...\n");
#endif
          json_object* comm = json_object_array_get_idx(comms, ii);
#ifdef DEBUG
          printf("Communication:\n%s\n\n", json_object_to_json_string(comm));
#endif
          // m_algolink->send_entity_message(json_object_get_string(json_object_object_get(comm, "sender")), "{time, src, dest, size, lifetime, x, y, group}");
          m_algolink->set_queued_communication_result(json_object_get_string(get(comm, "id")), "SUCCESS from CPP");
        }
      }

      json_object_put(comms);
    }
 
    /* Cleanup Memory */
    json_object_put(tick);
  }

  /*Logout (and check the logout functionality)*/
  printf("logout(): %s\n", m_algolink->logout() ? "true" : "false");
#ifdef DEBUG
  printf("is_loggedin(): %s\n", m_algolink->is_loggedin() ? "true": "false");
  printf("logout(): %s\n", m_algolink->logout() ? "true" : "false");
#endif
  /* Cleanup Memory */
  delete m_algolink;

  return 0;
}
