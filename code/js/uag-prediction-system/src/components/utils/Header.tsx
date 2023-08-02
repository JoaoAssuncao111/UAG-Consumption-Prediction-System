import React from "react";
import { Link } from "react-router-dom";
import { useState, useEffect } from "react";
import { api } from "../../api";

export function Header() {
    return (
        <div>
            <div className="home-container">
                <label className="header-title">UAG Consumption Prediction System</label>
                <h1 className="logo"></h1>
                <div className="logo1"></div>
                <div className="logo2"></div>
            </div>
            <Link className="link"to="/home">Home</Link>
        </div>
    );
}