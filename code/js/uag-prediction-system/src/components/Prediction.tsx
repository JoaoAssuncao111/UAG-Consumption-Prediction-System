import React, { useEffect, useState } from "react";
import { DatePickerInput } from './DatePicker';
import { api } from '../api';
import { format } from 'date-fns';
import "../styles.css"
import { Link } from "react-router-dom"
import { Header } from "./Header";

export function Prediction() {
  const [startDate, setStartDate] = useState(null);
  const [endDate, setEndDate] = useState(null);
  const [isFetchButtonDisabled, setisFetchButtonDisabled] = useState(true);
  const [error, setError] = useState("Por favor preencha a Janela Temporal");
  const [data, setData] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [selectedRoute, setSelectedRoute] = useState('training'); // Default selected route is 'training'
  const [timeoutId, setTimeoutId] = useState(undefined)

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
    const differenceInDays = Math.round(Math.abs((endDate - startDate) / (24 * 60 * 60 * 1000)));
    if (endDate === null || startDate === null) {
      setError('Por favor preencha a Janela Temporal');
      setisFetchButtonDisabled(true);
    } else if (endDate < startDate) {
      setError('Janela Temporal Inválida');
      setisFetchButtonDisabled(true);

    } else if (selectedRoute === 'prediction' && differenceInDays !== 4) {


      setError('Previsões são limitadas a 5 dias');
      setisFetchButtonDisabled(true);

    }
    else {
      setError("");
      setisFetchButtonDisabled(false);
    }
    console.log(differenceInDays)
  }, [startDate, endDate, selectedRoute]);

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
        setError('Valores de consumo inexistentes ou previsão já efetuada');
      }
    } catch (error) {
      setError('Ocorreu um erro');
    }

    setIsLoading(false);
  };

  const handleDateChange = (date, isStartDate) => {
    if (isStartDate) {
      setStartDate(date);
    } else {
      setEndDate(date);
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
        handleDateChange={(date) => handleDateChange(date, true)}
        placeholderText={"Data Inicial"}
      />
      <DatePickerInput
        selectedDate={endDate}
        handleDateChange={(date) => handleDateChange(date, false)}
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
