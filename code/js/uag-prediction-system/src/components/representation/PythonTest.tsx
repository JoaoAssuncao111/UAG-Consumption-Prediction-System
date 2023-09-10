import React, { useEffect, useState } from "react";
import { ReadingDataInput } from '../utils/ReadingDataInput';
import { api } from '../../api';
import { format } from 'date-fns';
import "../../styles.css"
import { Link } from "react-router-dom"
import { HumidityChart } from '../utils/HumidityChart';
import { Header } from "../utils/Header";

export function PythonTest(){
    const [string, setString] = useState("")

    useEffect(() => {
        fetch(`${api}/python`)
        .then((response) => {
            if (!response.ok) {
              throw new Error('Network response was not ok');
            }
            return response.text(); // Assuming the response is a string
          })
          .then((data) => {
            console.log(data)
            setString(data); // Set the result in your state
          })
          .catch((error) => {
            console.error('There was a problem with the fetch operation:', error);
          });
      }, []);

      return(
        <div>{string}</div>
      )
        
}
