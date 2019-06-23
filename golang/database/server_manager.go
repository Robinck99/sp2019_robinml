package main

import (
	"encoding/json"
	"./database_files"
	"fmt"
	"github.com/gorilla/mux"
	"net/http"
	"strconv"
)

var record [][]string
var attendencsRecord [][]string

func updateStudentRecord(writer http.ResponseWriter, request *http.Request) {
	params := mux.Vars(request)
	name := params["name"]
	rollno,_ := strconv.Atoi(params["rollno"])
	fmt.Println("student : ",name)
	fmt.Println("student : ",rollno)
	record = database_files.OverWriteRecord(record,name,rollno)
}

func addStudentRecord(writer http.ResponseWriter, request *http.Request) {
	params := mux.Vars(request)
	name := params["name"]
	rollno,_ := strconv.Atoi(params["rollno"])
	fmt.Println("student : ",name)
	fmt.Println("student : ",rollno)
	record = database_files.WriteRecord(record,name,rollno)
}

func getStudentRecord(writer http.ResponseWriter, request *http.Request) {
	record = database_files.ReadStudentRecords()
	_ = json.NewEncoder(writer).Encode(record)
}

func deleteStudentRecord(writer http.ResponseWriter, request *http.Request) {
	params := mux.Vars(request)
	rollno,_ := strconv.Atoi(params["rollno"])
	fmt.Println("student : ",rollno)
	record = database_files.RemoveRecord(record,rollno)
}

func getAttendenceRecord(writer http.ResponseWriter, request *http.Request) {
	var rec [][]string
	params := mux.Vars(request)
	dateTitle := params["date"]
	rec = database_files.ReadAttendenceRecords(dateTitle)
	_ = json.NewEncoder(writer).Encode(rec)
}

func setAttendenceRecord(writer http.ResponseWriter, request *http.Request) {
	params := mux.Vars(request)
	rollno := params["rollno"]
	attendencsRecord = database_files.SetAttendence(attendencsRecord,rollno)
	_ = json.NewEncoder(writer).Encode(attendencsRecord)
}

func getAttendenceStudentRecord(writer http.ResponseWriter, request *http.Request) {
	params := mux.Vars(request)
	rollno := params["rollno"]
	status := database_files.GetAttendence(attendencsRecord,rollno)
	_ = json.NewEncoder(writer).Encode(status)
}

func main() {
	database_files.CheckDir("student_data")
	record = database_files.ReadStudentRecords()
	attendencsRecord = database_files.SetAttendence(attendencsRecord,"11701026")
	router := mux.NewRouter()
	router.HandleFunc("/get_student_record", getStudentRecord).Methods("GET")
	router.HandleFunc("/add_student_record/{name}_{rollno}", addStudentRecord).Methods("POST")
	router.HandleFunc("/update_student_record/{name}_{rollno}", updateStudentRecord).Methods("POST")
	router.HandleFunc("/delete_student_record/{rollno}", deleteStudentRecord).Methods("POST")
	router.HandleFunc("/get_attendence_record/{date}", getAttendenceRecord).Methods("GET")
	router.HandleFunc("/get_attendence_student_record/{rollno}", getAttendenceStudentRecord).Methods("GET")
	router.HandleFunc("/set_attendence_record/{rollno}", setAttendenceRecord).Methods("POST")
	_ = http.ListenAndServe(":8000", router)
}