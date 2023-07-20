import React, { useEffect, useState } from "react";
import { DatePickerInput } from './DatePicker';
import { api } from '../api';
import { format } from 'date-fns';
import "../styles.css"
import { Link } from "react-router-dom"
import { Header } from "./Header";

export function Prediction() {
  const [startDate, setStartDate] = useState(undefined);
  const [endDate, setEndDate] = useState(undefined);
  const [isFetchButtonDisabled, setisFetchButtonDisabled] = useState(false);
  const [error, setError] = useState("");
  const [data, setData] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [selectedRoute, setSelectedRoute] = useState('training'); // Default selected route is 'training'

  useEffect(() => {
    setTimeout(() => {
      setError("");
    }, 3000);
  }, [error]);

  const handleRouteChange = (event) => {
    setSelectedRoute(event.target.value);
  };

  const handleButtonClick = async () => {
    const formattedStartDate = format(startDate, 'yyyy-MM-dd');
    const formattedEndDate = format(endDate, 'yyyy-MM-dd');
    setIsLoading(true);

    try {
      let resp;
      if (selectedRoute === 'training') {
        resp = await fetch(`${api}/training?startDate=${formattedStartDate}&endDate=${formattedEndDate}`, {
          method: 'PUT',
          headers: { 'content-type': 'application/json;charset=UTF-8' }
        });
      } else if (selectedRoute === 'prediction') {
        resp = await fetch(`${api}/predict?startDate=${formattedStartDate}&endDate=${formattedEndDate}`, {
          method: 'POST',
          headers: { 'content-type': 'application/json;charset=UTF-8' }
        });
      }

      if (resp.ok) {
        const responseData = await resp.json();
        setData(responseData);
      } else {
        setError('Ocorreu um erro');
      }
    } catch (error) {
      setError('Ocorreu um erro');
    }

    setIsLoading(false);
  };

  const handleStartDateChange = (date) => {
    setStartDate(date);
    if (endDate && date > endDate) {
      setError('Janela de tempo inválida');
      setisFetchButtonDisabled(true);
    } else {
      setisFetchButtonDisabled(false);
      setError('');
    }
  };

  const handleEndDateChange = (date) => {
    setEndDate(date);
    if (startDate && date < startDate) {
      setError('Janela de tempo inválida');
      setisFetchButtonDisabled(true);
    } else {
      setisFetchButtonDisabled(false);
      setError('');
    }
  };

  return (
    <div>
      <Header />
      <div className="route-selection">
        <label>
          <input
            type="radio"
            value="training"
            checked={selectedRoute === 'training'}
            onChange={handleRouteChange}
          />
          Treino
        </label>
        <label>
          <input
            type="radio"
            value="prediction"
            checked={selectedRoute === 'prediction'}
            onChange={handleRouteChange}
          />
          Previsão
        </label>
      </div>
      <button onClick={handleButtonClick} disabled={isFetchButtonDisabled}>
        {selectedRoute === 'training' ? 'Treinar UAGs' : 'Prever Consumos'}
      </button>
      <DatePickerInput
        selectedDate={startDate}
        handleDateChange={handleStartDateChange}
        placeholderText={"Data Inicial"}
      />
      <DatePickerInput
        selectedDate={endDate}
        handleDateChange={handleEndDateChange}
        placeholderText={"Data Final"}
      />
      {isLoading ? (
        <div className="loading-screen">Loading...</div>
      ) : (
        <div className="error-message">{error}</div>
      )}
    </div>
  );
}
