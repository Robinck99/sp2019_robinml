package main

import (
	"encoding/json"
	"github.com/gorilla/mux"
	"log"
	"net/http"
	"strconv"
)

var analogValue float64

var coma = "0"

func GetStatus(w http.ResponseWriter, r *http.Request) {
	_ = json.NewEncoder(w).Encode(analogValue)
}

func SetData(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	analogValue, _ = strconv.ParseFloat(params["val"],64)
	_ = json.NewEncoder(w).Encode(analogValue)
}

func setHeadCommand(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	coma = params["comma"]
	_ = json.NewEncoder(w).Encode(coma)
}

func getHeadCommand(w http.ResponseWriter, r *http.Request) {
	_ = json.NewEncoder(w).Encode(coma)
}

func main() {
	router := mux.NewRouter()
	analogValue = 0
	router.HandleFunc("/battery", GetStatus).Methods("GET")
	router.HandleFunc("/battery/{val}", SetData).Methods("POST")
	log.Fatal(http.ListenAndServe(":8000", router))
}