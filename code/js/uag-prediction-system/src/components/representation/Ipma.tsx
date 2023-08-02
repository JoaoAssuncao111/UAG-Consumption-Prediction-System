import * as React from "react";
import { Link } from "react-router-dom";
import { useState, useEffect } from "react";
import { api } from "../../api";

export function IPMA() {
    

    const handleClick = async() => {
        
            try {
                const resp = await fetch(`${api}/ipma`,{ method: 'POST' })
                const json = await resp.json()
                if (json) {
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
        </div>
    );
}