import React, { useEffect, useState } from "react";
import { ReadingDataInput } from './ReadingDataInput';
import { api } from '../api';
import { format } from 'date-fns';
import "../styles.css"
import { Link } from "react-router-dom"
import { HumidityChart } from './HumidityChart';

export function Humidity() {
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
                await fetch(`${api}/reading/humidity?startDate=${formattedStartDate}&endDate=${formattedEndDate}&location=${location}`)

            const json = await resp.json()
            if (await json) {
                const sortedArray = json.sort((a, b) => a.dateHour.localeCompare(b.dateHour));
                setData(sortedArray)
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
            <h1>{data.length == 0 ? null : "Gráfico de Humidade" }</h1>
            {data.length > 0 ? (
                <HumidityChart data={data} />
            ) : (
                null
            )}
        </div>
        </div>
    );
};

/*return (
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
                    <div className='table-cell'>Data {item.dateHour}</div>
                    <div className='table-cell'>Localidade {item.location}</div>
                    <div className='table-cell'>Previsão {item.predictionId}</div>
                    <div className='table-cell'>Humidade {item.value}</div>
                </div>)}
        </div>


    </div>
)
}
*/