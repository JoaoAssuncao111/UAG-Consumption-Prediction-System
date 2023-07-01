import React, { useEffect, useState } from 'react';
import { ReadingDataInput } from './ReadingDataInput';
import { api } from '../api';
import { format } from 'date-fns';
import { Link } from "react-router-dom"
import "../styles.css"
import { TemperatureChart } from './TemperatureChart'

export function Temperature() {
    const [location, setLocation] = useState(0);
    const [startDate, setStartDate] = useState(undefined);
    const [endDate, setEndDate] = useState(undefined);
    const [data, setData] = useState([])
    const [error, setError] = useState("")
    const [isFetchButtonDisabled, setisFetchButtonDisabled] = useState(false)

    useEffect(() => {
        setTimeout(() => {
            setError("")
        }, 3000);
      }, [error]);


   
    const handleSubmit = async () => {
        if (!(startDate && endDate)) {
            setError('Por favor insira a Janela Temporal');
            return
        }

        const formattedStartDate = format(startDate, 'yyyy-MM-dd');
        const formattedEndDate = format(endDate, 'yyyy-MM-dd');
        console.log(formattedEndDate)
        try {
            const resp =
                await fetch(`${api}/reading/temperature?startDate=${formattedStartDate}&endDate=${formattedEndDate}&location=${location}`)

            const json = await resp.json()
            if (await json) {
                const sortedArray = json.sort((a, b) => a.dateHour.localeCompare(b.dateHour));
                setData(sortedArray)
                console.log(json)
            }
            if( await data.length == 0) setError("Não existem leituras para os dados inseridos")
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
                    isFetchButtonDisabled={isFetchButtonDisabled}
                    setIsFetchButtonDisabled={setisFetchButtonDisabled}
                />
            </div>

            <div>
                <button disabled= {isFetchButtonDisabled} onClick={handleSubmit}>Fetch</button>
            </div>
            {data.length > 0 ? (
                <TemperatureChart data={data} />
            ) : null}
            <div>{error}</div>
        </div>
    );
}


