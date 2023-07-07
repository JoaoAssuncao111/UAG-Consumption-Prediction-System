import React, { useEffect, useState } from "react";
import { ReadingDataInput } from './ReadingDataInput';
import { api } from '../api';
import { format } from 'date-fns';
import "../styles.css"
import { Link, useParams, useHistory } from "react-router-dom"
import { HumidityChart } from './HumidityChart';
import { Header } from "./Header";


export function Uag() {
    const { name } = useParams()
    const [location, setLocation] = useState({
        observation: "",
        name: "",
        distance: 0,
        latitude: 0,
        longitude: 0
    })
    const [error, setError] = useState({})
    const history = useHistory();

    useEffect(() => {
        fetchUag()
    }, [])

    const fetchUag = async () => {
        try {
            const resp = await fetch(`${api}/uag/${name}`)
            const data = await resp.json()
            if (await data){ 
                setLocation(data)
            }
            else{history.push("/home")} 
        }
        catch (error) {
            setError("Erro na obtenção de dados da UAG")
        }

    }

    const handleDeleteClick = async () => {
        const resp = await fetch(`${api}/uag/${name}`, {
            method: 'DELETE',
            headers: { 'content-type': 'application/json;charset=UTF-8' },
        }
        )

        const data = await resp.json()

        if(await data == true) {history.push("/insertuag")}
    }
    return (
        <div>
            <Header></Header>
            <div className="align-right"><button className="delete-button" onClick={handleDeleteClick}>Apagar UAG</button></div>

            <div className="center-items">
                <div className="location-card">
                    <h3 className="location-name">{location.name}</h3>
                    <p className="location-observation">Observação: {location.observation}</p>
                    <div className="location-info">
                        <p className="location-info">Distancia a Sines: {location.distance} Km</p>
                        <p className="location-info">Latitude: {location.latitude}</p>
                        <p className="location-info">Longitude: {location.longitude}</p>
                    </div>
                </div>
            </div>
        </div>)
}