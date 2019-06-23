package database_files

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
	"time"
)

var rootDir = ""
var recordDir = ""

func CheckDir(fName string) bool {
	rootDir, _ = os.Getwd()
	rootDir += "/Record"
	workDir :=  rootDir + "/" + fName + ".csv"
	if fName == "student_data" {
		recordDir = workDir
	}
	if _,err := os.Stat(rootDir); os.IsNotExist(err) {
		_ = os.Mkdir(rootDir, 0755)
		_,_ = os.Create(workDir)
		fmt.Println("Created File and Folder.", fName)
		return true
	} else {
		if _,err := os.Stat(workDir); os.IsNotExist(err) {
			_, _ = os.Create(workDir)
			fmt.Println("Created File.", fName)
			return true
		} else {
			fmt.Println("File Exists.", fName)
			return false
		}
	}
}

func ReadStudentRecords() [][]string {
	var file,err = os.OpenFile(recordDir, os.O_RDWR, 0644)
	if isError(err) {return nil}
	defer file.Close()

	scanner := bufio.NewScanner(file)
	var records [][]string
	for scanner.Scan() {
		line := scanner.Text()
		s := strings.Split(line, ",")
		records = append(records, s)
	}
	return records
}

func WriteRecord(records [][]string, name string,rollno int) [][]string {
	if records == nil {
		records = ReadStudentRecords()
	}
	if containRollno(records,rollno) > 0 {
		fmt.Println("Already Present in Records")
		return records
	}
	entry := name + "," + strconv.Itoa(rollno) + "\n"
	var file,err = os.OpenFile(recordDir, os.O_APPEND | os.O_WRONLY, 0644)
	if isError(err) {return nil}
	defer file.Close()
	_,err = file.WriteString(entry)
	if isError(err) {return nil}
	err = file.Sync()
	if isError(err) {return nil}
	e := []string{name, strconv.Itoa(rollno)}
	records = append(records, e)
	fmt.Println("Record Entered Successfully",e)
	return records
}

func containRollno(records [][]string,rollno int) int {
	for i, a := range records {
		record,_ := strconv.Atoi(a[1])
		if record == rollno {
			return i
		}
	}
	return 0
}

func OverWriteRecord(records [][]string, name string,rollno int) [][]string {
	if records == nil {
		records = ReadStudentRecords()
	}
	index := containRollno(records,rollno)
	if index > 0 {
		if records[index][0] == name {
			fmt.Println("Already Present in Records")
			return records
		}
		records[index][0] = name
		var data = ""
		for _,a :=range records {
			data += a[0] + "," + a[1] + "\n"
		}
		var file,err = os.OpenFile(recordDir, os.O_RDWR, 0644)
		if isError(err) {return nil}
		defer file.Close()
		_ = file.Truncate(0)
		_, _ = file.Seek(0, 0)
		_,err = file.WriteString(data)
		if isError(err) {return nil}
		err = file.Sync()
		if isError(err) {return nil}
		fmt.Println("Record Over Written Successfully")
		return records
	}
	records = WriteRecord(records,name,rollno)
	return records
}

func isError(err error) bool {
	if err != nil {
		fmt.Println(err.Error())
	}
	return err != nil
}

func RemoveRecord(records [][]string,rollno int) [][]string {
	if records == nil {
		records = ReadStudentRecords()
	}
	index := containRollno(records,rollno)
	if index > 0 {
		records[index] = records[len(records)-1]
		records = records[:len(records) - 1]
		var data = ""
		for i,a :=range records {
			data += a[0] + "," + a[1] + "\n"
			fmt.Println(i)
		}
		var file,err = os.OpenFile(recordDir, os.O_RDWR, 0644)
		if isError(err) {return nil}
		defer file.Close()
		_ = file.Truncate(0)
		_, _ = file.Seek(0, 0)
		_,err = file.WriteString(data)
		if isError(err) {return nil}
		err = file.Sync()
		if isError(err) {return nil}
		fmt.Println("Record Removed Successfully")
		return records
	}
	fmt.Println("Record not Present")
	return records
}

func ReadAttendenceRecords(dateTitle string) [][]string {
	var file,err = os.OpenFile(rootDir + "/" + dateTitle + ".csv", os.O_RDWR, 0644)
	if isError(err) {return nil}
	defer file.Close()
	scanner := bufio.NewScanner(file)
	var attendanceRecord [][]string
	for scanner.Scan() {
		line := scanner.Text()
		s := strings.Split(line, ",")
		attendanceRecord = append(attendanceRecord, s)
	}
	return attendanceRecord
}

func WriteRecordToAttendanceRecord(attendenceRecord [][]string) [][]string {
	var records [][]string
	records = ReadStudentRecords()
	for _,record := range records {
		record = append(record, "0")
		attendenceRecord = append(attendenceRecord, record)
	}
	fmt.Println(attendenceRecord)
	var data = ""
	for _,a :=range attendenceRecord {
		data += a[0] + "," + a[1] + "," + a[2] +"\n"
	}
	dateTitle :=  time.Now().Format("2006_01_02")
	var file,err = os.OpenFile(rootDir + "/" + dateTitle + ".csv", os.O_RDWR, 0644)
	if isError(err) {return nil}
	defer file.Close()
	_ = file.Truncate(0)
	_, _ = file.Seek(0, 0)
	_,err = file.WriteString(data)
	if isError(err) {return nil}
	err = file.Sync()
	if isError(err) {return nil}
	fmt.Println("Record Transfer Written Successfully")
	return attendenceRecord
}

func SetupAttendence(attendencsRecord [][]string) [][]string {
	dateTitle :=  time.Now().Format("2006_01_02")
	if CheckDir(dateTitle) {
		attendencsRecord = WriteRecordToAttendanceRecord(attendencsRecord)
	} else {
		attendencsRecord = ReadAttendenceRecords(dateTitle)
	}
	return attendencsRecord
}

func GetAttendence(attendencRecord [][]string, rollno string) string {
	attendencRecord = SetupAttendence(attendencRecord)
	for _,a := range attendencRecord {
		if a[1] == rollno {
			return a[2]
		}
	}
	return "None"
}

func SetAttendence(attendencRecord [][]string, rollno string) [][]string {
	attendencRecord = SetupAttendence(attendencRecord)
	for _,a := range attendencRecord {
		if a[1] == rollno {
			if a[2] == "0" {
				a[2] = "1"
				var data = ""
				for _,a :=range attendencRecord {
					data += a[0] + "," + a[1] + "," + a[2] +"\n"
				}
				dateTitle :=  time.Now().Format("2006_01_02")
				var file,err = os.OpenFile(rootDir + "/" + dateTitle + ".csv", os.O_RDWR, 0644)
				if isError(err) {return nil}
				defer file.Close()
				_ = file.Truncate(0)
				_, _ = file.Seek(0, 0)
				_,err = file.WriteString(data)
				if isError(err) {return nil}
				err = file.Sync()
				if isError(err) {return nil}
				fmt.Println("Record Transfer Written Successfully")
				return attendencRecord
			} else {
				fmt.Println("Already Present")
				return attendencRecord
			}
		}
	}
	return attendencRecord
}