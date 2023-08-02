import React, { useEffect, useState } from 'react';
import { api } from '../../api';
import { Header } from '../utils/Header';
import {useHistory ,Link } from "react-router-dom";

export function InsertUag() {
    const [formInputData, setFormInputData] = useState({
        observation: "",
        name: "",
        distance: 0.0,
        latitude: 0.0,
        longitude: 0.0,
    })
    const [locations, setLocations] = useState([]);
    const history = useHistory();

    useEffect(() => {
        fetchLocations()
    },[])
    
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
    

    const handleChange = (event) => {

        setFormInputData({
            ...formInputData,
            [event.target.name]: event.target.value
        })
        console.log(event.target.name)
        console.log(event.target.value)
    }

    const handleSubmit = async (event) => {
        event.preventDefault();
        console.log(formInputData)
        try {
            const resp =
                await fetch(`${api}/uags`, {
                    method: 'POST',
                    headers: { 'content-type': 'application/json;charset=UTF-8' },
                    body: JSON.stringify(formInputData),
                })
            const data = await resp.json()


        }
        catch (error) {
            console.log(error)
        }
        fetchLocations()
    }

    const handleLocationClick = (name) => {
        history.push(`/uag/${name}`)
    }

    return (
        <div>
            <Header></Header>
            <form>
                <h3>Inserir Nova UAG</h3>
                <input type="text" placeholder="Observação" name="observation" onChange={handleChange} />
                <input type="text" placeholder="Nome" name="name" onChange={handleChange} />
                <input type="text" placeholder="Distância" name="distance" onChange={handleChange} />
                <input type="text" placeholder="Latitude" name="latitude" onChange={handleChange} />
                <input type="text" placeholder="Longitude" name="longitude" onChange={handleChange} />
                <button type='submit' onClick={handleSubmit}>Inserir</button>
            </form>

            <div className="locations-list">
                <h3>UAGS</h3>
                <div className="scroll-box">
                    {locations.map((location) => (
                        <div onClick={() => handleLocationClick(location.name)} className='location-item' key={location.id}>{location.name}</div>
                    ))}
                </div>
            </div>
        </div>
    )


}