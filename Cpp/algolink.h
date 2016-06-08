#include <string>
#include <list>
#include <json/json.h>

#ifndef ALGOLINK_H
#define ALGOLINK_H

using namespace std;

namespace artistech
{
class AlgoLink {
private:
  string _server;
  string _id;

  json_object *do_get(string function);
  json_object *do_post(string function, json_object *params);

protected:
  list<string> get_list(string function);

public: AlgoLink (string server);
  AlgoLink (const AlgoLink& copy);

  string get_server();
  string get_id();
  string login();
  bool logout();
  bool is_loggedin();
  string get_version();
  string send_entity_message(string entity_id, string message);
  json_object *get_config_schema(string type);
  // list<string> get_all_config_types();
  list<string> get_organization_types();
  // list<string> get_data_builders();
  list<string> get_communication_filters();
  list<string> get_entities();
  // list<string> get_edge_entities();
  // list<string> get_ummf_entities();
  json_object *get_communications();
  json_object *get_positions();
  json_object *get_db_config();
  json_object *get_population();
  json_object *tick();
  json_object *create_population(json_object *configs);
  json_object *create_population(json_object *configs, bool enable_tick_support);
  json_object *create_population(json_object *configs, bool enable_tick_support, list<string> &outputs);
  json_object *get_queued_communications();
  string set_queued_communication_result(string id, string result);
  string set_queued_communication_received(string id, string result);
  string set_queued_communication_cancel(string id, string result);
  string set_queued_communication_start(string id, string result);
  string set_queued_communication_result_2(string results);

  // list<string> get_diversity_caches();
  // list<string> get_diversity_entities();
};
};

#endif /* ALGOLINK_H */
