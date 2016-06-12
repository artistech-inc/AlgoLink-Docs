#include "algolink.h"

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stddef.h>
#include <iostream>

#include <json/json.h>
#include <curl/curl.h>

struct MemoryStruct {
  char *memory;
  size_t size;
};

static size_t WriteMemoryCallback(void *contents, size_t size, size_t nmemb, void *userp) {
  size_t realsize = size * nmemb;
  struct MemoryStruct *mem = (struct MemoryStruct *)userp;

  mem->memory = (char *)realloc(mem->memory, mem->size + realsize + 1);
  if (mem->memory == NULL) {
    /* out of memory! */
    printf("not enough memory (realloc returned NULL)\n");
    exit(EXIT_FAILURE);
  }

  memcpy(&(mem->memory[mem->size]), contents, realsize);
  mem->size += realsize;
  mem->memory[mem->size] = 0;

  return realsize;
}

json_object *artistech::AlgoLink::do_post(string function, json_object *params = NULL) {
  CURL *curl;

  struct MemoryStruct chunk;
  struct json_object *new_obj;

  struct curl_httppost *formpost = NULL;
  struct curl_httppost *lastptr = NULL;
  struct curl_slist *headerlist = NULL;
  static const char buf[] = "Expect:";

  string url = this->_server + function;

  chunk.memory = (char *)malloc(1); /* will be grown as needed by the realloc above */
  chunk.size = 0;                   /* no data at this point */

  curl_global_init(CURL_GLOBAL_ALL);

  /* Put the ID in the form */
  curl_formadd(&formpost,
               &lastptr,
               CURLFORM_COPYNAME, "algolink_id",
               CURLFORM_COPYCONTENTS, this->_id.c_str(),
               CURLFORM_END);

  /* Put the params in the form */
  if (params != NULL) {
    json_object_object_foreach(params, key, obj) {
      int type_check = json_object_is_type(obj, json_type_string);

      if (type_check != 0) {
        curl_formadd(&formpost,
                     &lastptr,
                     CURLFORM_COPYNAME, key,
                     CURLFORM_COPYCONTENTS, json_object_get_string(obj),
                     CURLFORM_END);
      }
      else{
        curl_formadd(&formpost,
                     &lastptr,
                     CURLFORM_COPYNAME, key,
                     CURLFORM_COPYCONTENTS, json_object_to_json_string(obj),
                     CURLFORM_END);
      }
    }
  }

  curl = curl_easy_init();
  /* initalize custom header list (stating that Expect: 100-continue is not
     wanted */
  headerlist = curl_slist_append(headerlist, buf);
  if (curl) {
    /* what URL that receives this POST */
    /* send all data to this function  */
    curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, WriteMemoryCallback);
    curl_easy_setopt(curl, CURLOPT_POST, 1);

    /* we pass our 'chunk' struct to the callback function */
    curl_easy_setopt(curl, CURLOPT_WRITEDATA, (void *)&chunk);

    /* some servers don't like requests that are made without a user-agent
       field, so we provide one */
    curl_easy_setopt(curl, CURLOPT_USERAGENT, "libcurl-agent/1.0");

    curl_easy_setopt(curl, CURLOPT_URL, url.c_str());
//        if ( (argc == 2) && (!strcmp(argv[1], "noexpectheader")) )
//            /* only disable 100-continue header if explicitly requested */
//            curl_easy_setopt(curl, CURLOPT_HTTPHEADER, headerlist);

    curl_easy_setopt(curl, CURLOPT_HTTPPOST, formpost);
    curl_easy_perform(curl);


    /* always cleanup */
    curl_easy_cleanup(curl);

    /* then cleanup the formpost chain */
    curl_formfree(formpost);
    /* free slist */
    curl_slist_free_all(headerlist);
#ifdef DEBUG
    printf("%lu bytes retrieved\n", (long)chunk.size);
#endif
    if (chunk.memory) {
#ifdef DEBUG
      printf("%s\n", chunk.memory);
#endif
      new_obj = json_tokener_parse(chunk.memory);
#ifdef DEBUG
      printf("new_obj.to_string()=%s\n", json_object_to_json_string(new_obj));
#endif
      free(chunk.memory);

      return new_obj;
    }
  }
  return NULL;
}

json_object *artistech::AlgoLink::do_get(string function) {
  CURL *curl;

  struct MemoryStruct chunk;
  struct json_object *new_obj;

  chunk.memory = (char *)malloc(1); /* will be grown as needed by the realloc above */
  chunk.size = 0;                   /* no data at this point */

  curl_global_init(CURL_GLOBAL_ALL);

  /* init the curl session */
  curl = curl_easy_init();

  if (curl) {
    /* specify URL to get */
    string url = this->_server + function;
    curl_easy_setopt(curl, CURLOPT_URL, url.c_str());

    /* send all data to this function  */
    curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, WriteMemoryCallback);

    /* we pass our 'chunk' struct to the callback function */
    curl_easy_setopt(curl, CURLOPT_WRITEDATA, (void *)&chunk);

    /* some servers don't like requests that are made without a user-agent
       field, so we provide one */
    curl_easy_setopt(curl, CURLOPT_USERAGENT, "libcurl-agent/1.0");

    /* get it! */
    curl_easy_perform(curl);

    /* cleanup curl stuff */
    curl_easy_cleanup(curl);

    /*
     * Now, our chunk.memory points to a memory block that is chunk.size
     * bytes big and contains the remote file.
     *
     * Do something nice with it!
     *
     * You should be aware of the fact that at this point we might have an
     * allocated data block, and nothing has yet deallocated that data. So when
     * you're done with it, you should free() it as a nice application.
     */

  #ifdef DEBUG
    printf("%lu bytes retrieved\n", (long)chunk.size);
  #endif
    if (chunk.memory) {
      new_obj = json_tokener_parse(chunk.memory);
  #ifdef DEBUG
      printf("new_obj.to_string()=%s\n", json_object_to_json_string(new_obj));
  #endif
      free(chunk.memory);
      return new_obj;
    }
  }

  cout << "ALGOLINK ERROR: Unable to do_get(" << function << ")" << endl;
  exit(1);
}

artistech::AlgoLink::AlgoLink (string server) {
  this->_id = "";
  this->_server = server;
}

artistech::AlgoLink::AlgoLink (const AlgoLink& copy) {
  this->_id = copy._id;
  this->_server = copy._server;
}

string artistech::AlgoLink::get_server() {
  return this->_server;
}

string artistech::AlgoLink::get_id() {
  return this->_id;
}

string artistech::AlgoLink::login() {
  if (!this->is_loggedin()) {
    json_object *res = do_get("login.json");
    this->_id = string(json_object_get_string(res));
    json_object_put(res);
  }
  return this->_id;
}

bool artistech::AlgoLink::is_loggedin() {
  return this->_id != "";
}

bool artistech::AlgoLink::logout() {
  bool ret = this->is_loggedin();

  if (ret) {
    do_post("logout.json");
    //json_object *res = do_post("logout.json");
    //json_object_put(res);
    this->_id = "";
  }
  return ret;
}

string artistech::AlgoLink::get_version() {
  json_object *res = do_get("version.json");
  string ret = string(json_object_get_string(res));

  json_object_put(res);
  return ret;
}

string artistech::AlgoLink::send_entity_message(string entity_id, string message) {
  json_object *params = json_object_new_object();

  json_object_object_add(params, "id", json_object_new_string(entity_id.c_str()));
  json_object_object_add(params, "message", json_object_new_string(message.c_str()));

  json_object *res = do_post("send_entity_message.json", params);
  string ret = string(json_object_get_string(res));
  json_object_put(res);
  json_object_put(params);
  return ret;
}

json_object *artistech::AlgoLink::get_config_schema(string type) {
  json_object *params = json_object_new_object();
  json_object *type_obj = json_object_new_string(type.c_str());

  json_object_object_add(params, "type", type_obj);

  json_object *res = do_post("configSchema.json", params);
  return res;
}

// list<string> artistech::AlgoLink::get_all_config_types() {
//   return this->get_list("allConfigTypes.json");
// }

list<string> artistech::AlgoLink::get_organization_types() {
  return this->get_list("organizationTypes.json");
}

// list<string> artistech::AlgoLink::get_data_builders() {
//   return this->get_list("dataBuilders.json");
// }

list<string> artistech::AlgoLink::get_communication_filters() {
  return this->get_list("communicationFilters.json");
}

list<string> artistech::AlgoLink::get_entities() {
  list<string> lst;
  if (this->is_loggedin()) {
    lst = this->get_list("entities.json");
  }
  return lst;
}

// list<string> artistech::AlgoLink::get_edge_entities() {
//   list<string> lst;
//   if (this->is_loggedin()) {
//     lst = this->get_list("edgeEntities.json");
//   }
//   return lst;
// }

// list<string> artistech::AlgoLink::get_ummf_entities() {
//   list<string> lst;
//   if (this->is_loggedin()) {
//     lst = this->get_list("ummfEntities.json");
//   }
//   return lst;
// }

json_object *artistech::AlgoLink::get_communications() {
  if (this->is_loggedin()) {
    json_object *res = do_post("getCommunications.json");
    return res;
  }
  return NULL;
}


json_object *artistech::AlgoLink::get_positions() {
  if (this->is_loggedin()) {
    json_object *res = do_post("getCommunications.json");
    return res;
  }
  return NULL;
}

json_object *artistech::AlgoLink::get_db_config() {
  if (this->is_loggedin()) {
    json_object *res = do_post("getDBConfig.json");
    return res;
  }
  return NULL;
}

json_object *artistech::AlgoLink::get_population() {
  if (this->is_loggedin()) {
    json_object *res = do_post("getPopulation.json");
    return res;
  }
  return NULL;
}

json_object *artistech::AlgoLink::tick() {
  if (this->is_loggedin()) {
    json_object *res = do_post("tick.json");
    return res;
  }
  return NULL;
}

json_object *artistech::AlgoLink::create_population(json_object *configs) {
  list<string> outputs;
  return this->create_population(configs, true, outputs);
}

json_object *artistech::AlgoLink::create_population(json_object *configs, bool enable_tick_support) {
  list<string> outputs;
  return this->create_population(configs, enable_tick_support, outputs);
}

json_object *artistech::AlgoLink::create_population(json_object *configs, bool enable_tick_support, list<string> &outputs) {
  if (this->is_loggedin()) {
    json_object *params = json_object_new_object();
    json_object *tick_support = json_object_new_boolean(enable_tick_support);
    json_object *outs = json_object_new_array();
    for (list<string>::iterator it = outputs.begin(); it != outputs.end(); it++) {
      json_object_array_add(outs, json_object_new_string((*it).c_str()));
    }
    json_object_object_add(params, "configs", configs);
    json_object_object_add(params, "enable_tick_support", tick_support);
    json_object_object_add(params, "outputs", outs);

    json_object *res = do_post("createPopulation.json", params);
    json_object_put(params);
    return res;
  }
  return NULL;
}

json_object *artistech::AlgoLink::get_queued_communications() {
  if (this->is_loggedin()) {
    json_object *res = do_post("GetQueuedWebComms");
    return res;
  }
  return NULL;
}

string artistech::AlgoLink::set_queued_communication_cancel(string comm_id, string result) {
  if (this->is_loggedin()) {
    json_object *params = json_object_new_object();
    json_object *comm_id_obj = json_object_new_string(comm_id.c_str());
    json_object *result_obj = json_object_new_string(result.c_str());

    json_object_object_add(params, "commId", comm_id_obj);
    json_object_object_add(params, "result", result_obj);

    json_object *res = do_post("CancelQueuedCommunication", params);
    string ret = json_object_get_string(res);
    json_object_put(res);
    json_object_put(params);
    return ret;
  }
  return NULL;
}

string artistech::AlgoLink::set_queued_communication_start(string comm_id, string result) {
  if (this->is_loggedin()) {
    json_object *params = json_object_new_object();
    json_object *comm_id_obj = json_object_new_string(comm_id.c_str());
    json_object *result_obj = json_object_new_string(result.c_str());

    json_object_object_add(params, "commId", comm_id_obj);
    json_object_object_add(params, "result", result_obj);

    json_object *res = do_post("StartQueuedCommunication", params);
    string ret = json_object_get_string(res);
    json_object_put(res);
    json_object_put(params);
    return ret;
  }
  return NULL;
}

string artistech::AlgoLink::set_queued_communication_received(string comm_id, string result) {
  if (this->is_loggedin()) {
    json_object *params = json_object_new_object();
    json_object *comm_id_obj = json_object_new_string(comm_id.c_str());
    json_object *result_obj = json_object_new_string(result.c_str());

    json_object_object_add(params, "commId", comm_id_obj);
    json_object_object_add(params, "result", result_obj);

    json_object *res = do_post("ReceivedQueuedCommunication", params);
    string ret = json_object_get_string(res);
    json_object_put(res);
    json_object_put(params);
    return ret;
  }
  return NULL;
}

string artistech::AlgoLink::set_queued_communication_result(string comm_id, string result) {
  if (this->is_loggedin()) {
    json_object *params = json_object_new_object();
    json_object *comm_id_obj = json_object_new_string(comm_id.c_str());
    json_object *result_obj = json_object_new_string(result.c_str());

    json_object_object_add(params, "commId", comm_id_obj);
    json_object_object_add(params, "result", result_obj);

    json_object *res = do_post("SetQueuedWebCommunicationResult", params);
    string ret = json_object_get_string(res);
    json_object_put(res);
    json_object_put(params);
    return ret;
  }
  return NULL;
}

string artistech::AlgoLink::set_queued_communication_result_2(string results) {
  if (this->is_loggedin()) {
    json_object *params = json_object_new_object();
    json_object *results_obj = json_object_new_string(results.c_str());

    json_object_object_add(params, "results", results_obj);

    json_object *res = do_post("SetQueuedWebCommunicationResult", params);
    string ret = json_object_get_string(res);
    json_object_put(res);
    json_object_put(params);
    return ret;
  }
  return NULL;
}

// list<string> artistech::AlgoLink::get_diversity_caches() {
//   list<string> lst;
//   if (this->is_loggedin()) {
//     lst = this->get_list("GetDiversityCaches.json");
//   }
//   return lst;
// }

// list<string> artistech::AlgoLink::get_diversity_entities() {
//   list<string> lst;
//   if (this->is_loggedin()) {
//     lst = this->get_list("GetDiversityEntities.json");
//   }
//   return lst;
// }

list<string> artistech::AlgoLink::get_list(string function) {
  list<string> lst;
  json_object *res = do_post(function);
  int count = json_object_array_length(res);

  for (int ii = 0; ii < count; ii++) {
    string type = json_object_get_string(json_object_array_get_idx(res, ii));
    lst.push_back(type);
  }

  json_object_put(res);

  return lst;
}

