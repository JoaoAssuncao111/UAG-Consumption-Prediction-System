import React, { useEffect, useState } from 'react';
import { ReadingDataInput } from './ReadingDataInput';
import DatePicker from 'react-datepicker';
import { api } from '../api';
import { format } from 'date-fns';

export function Temperature() {
    const [location, setLocation] = useState(0);
    const [startDate, setStartDate] = useState(undefined);
    const [endDate, setEndDate] = useState(undefined);
    const [data, setData] = useState([])
    

    const handleSubmit = async () => {
        if(!(startDate && endDate)){
            console.log("error")
            return}
        const options = { year: 'numeric', month: '2-digit', day: '2-digit' };
        const formattedStartDate = format(startDate, 'yyyy-MM-dd');
    const formattedEndDate = format(endDate, 'yyyy-MM-dd');
        console.log(formattedEndDate)
        try {
            const resp =
                await fetch(`${api}/reading/temperature?startDate=${formattedStartDate}&endDate=${formattedEndDate}&location=${location}`)

            const json = await resp.json()
            if (await json) {
                setData(json)
                console.log(json)
            }
        } catch (error) {
            console.log(error)
        }
    }
    return (
        <div>
            <div>
                <ReadingDataInput 
                    location={location}
                    setLocation={setLocation}
                    startDate={startDate} 
                    setStartDate={setStartDate}
                    endDate={endDate}
                    setEndDate={setEndDate}
                />
            </div>
            <div>
                <button onClick={handleSubmit}>Fetch</button>
            </div>

            
        </div>
    )
}