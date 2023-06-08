import React, { useEffect, useState } from 'react';
import { ReadingDataInput } from './ReadingDataInput';
import { api } from '../api';
import { format } from 'date-fns';
import "../styles.css"
import {Link} from "react-router-dom"

export function Levels() {
    const [location, setLocation] = useState(0);
    const [startDate, setStartDate] = useState(undefined);
    const [endDate, setEndDate] = useState(undefined);
    const [data, setData] = useState([])


    const handleSubmit = async () => {
        if (!(startDate && endDate)) {
            console.log("error")
            return
        }
        const formattedStartDate = format(startDate, 'yyyy-MM-dd');
        const formattedEndDate = format(endDate, 'yyyy-MM-dd');
        console.log(formattedEndDate)
        try {
            const resp =
                await fetch(`${api}/reading/levels?startDate=${formattedStartDate}&endDate=${formattedEndDate}&location=${location}`)

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
                <Link to="/home">Home</Link>
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
            <div className='table-container'>
                {data.map(item =>
                    <div className="table-row" key={item.username}>
                        <div className='table-cell'>Id {item.id}</div>
                        <div className='table-cell'>Data {item.date}</div>
                        <div className='table-cell'>Nível de Gás {item.gasLevel}</div>
                        <div className='table-cell'>Localidade {item.location}</div>
                        <div className='table-cell'>Nº Depósito {item.depositNumber}</div>
                        <div className='table-cell'>Contador {item.counter}</div>
                        <div className='table-cell'>Consumo {item.consumption}</div>
                    </div>)}
            </div>


        </div>
    )
}