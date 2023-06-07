import * as React from "react";
import { Link } from "react-router-dom";
import { useState, useEffect } from "react";
import { api } from "../api";

export function Uags() {
    const [data, setData] = useState([])

    const handleClick = async() => {
        
            try {
                const resp = await fetch(`${api}/uags`,{ method: 'GET' })
                const json = await resp.json()
                if (json) {
                    setData(json)
                    console.log(json)
                }
            } catch (error) {
                console.log(error)
            }

    } 
    return (
        <div>
            <Link to="/home">Home</Link>
            <button onClick={handleClick}>Fetch</button>
            <table className="leaderboard">
                <thead>
                    <tr>
                        <th>ID UAG</th>
                        <th>Observação</th>
                        <th>Localidade</th>
                        <th>Distância a Sines</th>
                        <th>Latitude</th>
                        <th>Longitude</th>
                    </tr>
                </thead>
                <tbody>
                    {data.map((item) => (
                        <tr key={item.id}>
                            <td>{item.id}</td>
                            <td>{item.observation}</td>
                            <td>{item.name}</td>
                            <td>{item.distance}</td>
                            <td>{item.latitude}</td>
                            <td>{item.longitude}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}