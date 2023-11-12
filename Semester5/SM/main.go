package main

import (
	"fmt"
	"strconv"
)

func main() {
	dataAsli := "AAAAooooPPPPPlLLLhhhhTTTTTEBHNMMM"
	encodeData := Encode(dataAsli)
	fmt.Printf("Data Asli: %s\n", dataAsli)
	fmt.Printf("Data Encode: %s\n", encodeData)
}

func Encode(data string) string {
	encoded := ""
	count := 1

	for i := 1; i < len(data); i++ {
		if data[i] == data[i-1] {
			count++
		} else {
			if count == 1 {
				encoded += string(data[i-1])
			} else {
				encoded += string(data[i-1]) + strconv.Itoa(count)
			}
			count = 1
		}
	}
	if count == 1 {
		encoded += string(data[len(data)-1])
	} else {
		encoded += string(data[len(data)-1]) + strconv.Itoa(count)
	}

	return encoded
}
