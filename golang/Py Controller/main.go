package main

import (
	"encoding/json"
	"fmt"
	"github.com/gorilla/mux"
	"net/http"
)

var status = true
var command_data = ""

func post_data(writer http.ResponseWriter, request *http.Request) {
	params := mux.Vars(request)
	commad := params["command_data"]
	if commad != "" {
		status = true
		command_data = commad
	} else {
		status = false
	}
}

func checkStatus(writer http.ResponseWriter, request *http.Request) {
	c := 0
	if status { c = 1}
	_ = json.NewEncoder(writer).Encode(c)
}

func setStatus(writer http.ResponseWriter, request *http.Request) {
	_ = json.NewEncoder(writer).Encode(status)
}

func getCommadData(writer http.ResponseWriter, request *http.Request) {
	status = false
	_ = json.NewEncoder(writer).Encode(command_data)
	fmt.Println(status)
}

func main() {
	router := mux.NewRouter()
	router.HandleFunc("/post_data/{command_data}", post_data).Methods("POST")
	router.HandleFunc("/check_status/", checkStatus).Methods("GET")
	router.HandleFunc("/set_status/", setStatus).Methods("POST")
	router.HandleFunc("/get_command_data/", getCommadData).Methods("GET")
	_ = http.ListenAndServe(":8000", router)
}
