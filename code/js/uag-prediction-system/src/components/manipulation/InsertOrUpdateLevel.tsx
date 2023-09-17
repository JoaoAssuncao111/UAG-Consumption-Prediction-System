import React, { useEffect, useState } from 'react';
import { api } from "../../api";
import { ReadingDataInput } from '../utils/ReadingDataInput';
import { DatePickerInput } from '../utils/DatePicker';
import { Header } from '../utils/Header';
import Select from 'react-select';
import { format } from 'date-fns';

export function InsertOrUpdateLevel() {
    const [formInputData, setFormInputData] = useState({
        level: undefined,
        location: undefined,
        depositNumber: undefined,
        counter: undefined,
        consumption: undefined
    })

    const [locations, setLocations] = useState([])
    const [isFetchButtonDisabled, setIsFetchButtonDisabled] = useState(false)
    const [selectedOption, setSelectedOption] = useState(null);
    const [rawDate, setDate] = useState(new Date())
    const [error,setError] = useState("")
    const [timeoutId, setTimeoutId] = useState(undefined)
    const options = locations.map((location) => ({
        value: location.id,
        label: location.name,
    }));


    useEffect(() => {
        fetchLocations()
    }, [])

    useEffect(() => {

        clearTimeout(timeoutId);
        if (error) {
          let newTimeoutId = setTimeout(() => {
            setError("");
          }, 3000);
          setTimeoutId(newTimeoutId)
        }
    
        return () => {
          clearTimeout(timeoutId);
        };
      }, [error]);

    useEffect(() => {
        if(isNaN(formInputData.location) || (isNaN(formInputData.level) && formInputData.level < 0) || (isNaN(formInputData.depositNumber) && formInputData.depositNumber < 0)
         || isNaN(formInputData.counter) || isNaN(formInputData.consumption)){
            setError("Insira os dados em falta")
            setIsFetchButtonDisabled(true)
        }
        else{
            setError("")
            setIsFetchButtonDisabled(false)
        }

        console.log(formInputData.level)
    },[formInputData])
   

    const handleChange = (event) => {
        var { name, value } = event.target;
        if(name == 'level' || name == 'consumption') value = parseFloat(value)    
        setFormInputData({
            ...formInputData,
            [name]:  value, 
        });
    };

    const handleDateChange = (date) => {
        
        setDate(date)
    }

    const handleLocationChange = (location) => {
        
        setSelectedOption(location);
        setFormInputData({
            ...formInputData,
            location: location.value
        })
    }

    const handleSubmit = async (event) => {
         
        const date = format(rawDate, 'yyyy-MM-dd');
        console.log(JSON.stringify({date,...formInputData}))
        event.preventDefault();
        await fetch(`${api}/levels`, {
            method: 'PUT',
            headers: { 'content-type': 'application/json;charset=UTF-8' },
            body: JSON.stringify({date,...formInputData}),
        })
    }

    const fetchLocations = async () => {
        try {
            const resp = await fetch(`${api}/uags`);
            const data = await resp.json();

            if (data) {
                const sortedLocations = data.sort((a, b) => a.name.localeCompare(b.name))
                setLocations(sortedLocations);
                console.log(sortedLocations);
            }
        } catch (error) {
            console.log(error);
        }
    };




    return (
        <div>
            <Header></Header>
            <h3>Inserir/Atualizar Níveis</h3>
            <Select className='select-bar'
                value={selectedOption}
                onChange={handleLocationChange}
                options={options}
                placeholder="Selecione uma localização"
                isSearchable
            ></Select>


            <form>

                <DatePickerInput selectedDate={rawDate} handleDateChange={(date) => handleDateChange(date)} placeholderText={"Data"} />
                <input type="number" placeholder="Nível de Gás" name="level"  onChange={handleChange} />
                <input type="number" placeholder="Número de Depósito" name="depositNumber" onChange={handleChange} />
                <input type="number" placeholder="Valor de Contador" name="counter" onChange={handleChange} />
                <input type="number" placeholder="Consumo" name="consumption" onChange={handleChange} />
                <button type='submit' disabled={isFetchButtonDisabled} onClick={handleSubmit}>Inserir</button>

            </form>
            <div className="error-message">{error}</div>

        </div>);
}