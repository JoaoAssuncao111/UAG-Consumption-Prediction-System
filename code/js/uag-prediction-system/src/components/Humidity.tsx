import React, { useEffect, useState } from "react";
import { ReadingDataInput } from './ReadingDataInput';
import { api } from '../api';
import { format } from 'date-fns';
import "../styles.css"
import { Link } from "react-router-dom"
import { HumidityChart } from './HumidityChart';
import { Header } from "./Header";

export function Humidity() {
    const [location, setLocation] = useState(0);
    const [startDate, setStartDate] = useState(undefined);
    const [endDate, setEndDate] = useState(undefined);
    const [data, setData] = useState([])
    const [isFetchButtonDisabled, setisFetchButtonDisabled] = useState(false)
    const [error, setError] = useState("")

    useEffect(() => {
        setTimeout(() => {
            setError("")
        }, 3000);
    }, [error]);

    const handleSubmit = async () => {
        console.log(location)
        if (!(startDate && endDate)) {
            console.log("error")
            return
        }
        const formattedStartDate = format(startDate, 'yyyy-MM-dd');
        const formattedEndDate = format(endDate, 'yyyy-MM-dd');
        console.log(formattedEndDate)
        try {
            const resp =
                await fetch(`${api}/reading/humidity?startDate=${formattedStartDate}&endDate=${formattedEndDate}&location=${location}`)

            const json = await resp.json()
            if (await json) {
                const sortedArray = json.sort((a, b) => a.dateHour.localeCompare(b.dateHour));
                setData(sortedArray)
                if (json.length === 0) setError("Não existem leituras para os dados inseridos")
            }
        } catch (error) {
            console.log(error)
        }
    }
    return (
        <div>
            <Header></Header>
            <div>
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
                <button disabled={isFetchButtonDisabled} onClick={handleSubmit}>Fetch</button>
                <h1 className="chart-title">{data.length == 0 ? null : "Gráfico de Humidade"}</h1>
                {data.length > 0 ? (
                    <HumidityChart data={data} />
                ) : (
                    null
                )}
            </div>
            <div>{error}</div>
        </div>
    );
};

