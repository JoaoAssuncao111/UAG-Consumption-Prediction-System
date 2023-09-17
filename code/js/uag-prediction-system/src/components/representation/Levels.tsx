import React, { useEffect, useState } from 'react';
import { ReadingDataInput } from '../utils/ReadingDataInput';
import { api } from "../../api";
import { format } from 'date-fns';
import { Link } from 'react-router-dom';
import { LevelsChart } from '../utils/LevelsChart';
import { Header } from "../utils/Header";

export function Levels() {
  const [location, setLocation] = useState(0);
  const [startDate, setStartDate] = useState(undefined);
  const [endDate, setEndDate] = useState(undefined);
  const [selectedDeposit, setSelectedDeposit] = useState('all');
  const [levelData, setLevelData] = useState([]);
  const [deliveryData, setDeliveryData] = useState([]);
  const [isFetchButtonDisabled, setisFetchButtonDisabled] = useState(false)
  const [error, setError] = useState("")


  useEffect(() => {
    setTimeout(() => {
      setError("")
    }, 3000);
  }, [error]);

  const handleSubmit = async () => {
    if (!(startDate && endDate)) {
      console.log('error');
      return;
    }
    const formattedStartDate = format(startDate, 'yyyy-MM-dd');
    const formattedEndDate = format(endDate, 'yyyy-MM-dd');
  
    try {
      const resp = await fetch(
        `${api}/reading/levels?startDate=${formattedStartDate}&endDate=${formattedEndDate}&location=${location}`
      );

      const json = await resp.json();
      if (json) {
        setLevelData(json);

        if (json.length === 0) setError("Não existem leituras para os dados inseridos")
      }
    } catch (error) {
      console.log(error);
    }

    try {
      const resp = await fetch(
        `${api}/deliveries?startDate=${formattedStartDate}&endDate=${formattedEndDate}&location=${location}`
      );

      const json = await resp.json();
      if (json) {      
        setDeliveryData(json);
      
      }
    } catch (error) {
      console.log(error)
    }
  };

  const filterDataByDeposit = () => {
    if (selectedDeposit === 'all') {
      return levelData;
    } else {
      const depositNumber = parseInt(selectedDeposit); // Convert selectedDeposit to integer
      console.log(levelData.filter((item) => item.depositNumber === depositNumber))
      return levelData.filter((item) => item.depositNumber === depositNumber);
    }

    
  };

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
        <h1 className='chart-title'>{levelData.length === 0 ? null : 'Gráfico de Níveis e Entregas'}</h1>
        {levelData.length > 0 ? (
          <>
            <div>
              <input
                type="radio"
                id="all"
                name="deposit"
                value="all"
                checked={selectedDeposit === 'all'}
                onChange={() => setSelectedDeposit('all')}
              />
              <label htmlFor="all">All Deposits</label>
            </div>
            <div>
              <input
                type="radio"
                id="deposit1"
                name="deposit"
                value="1"
                checked={selectedDeposit === '1'}
                onChange={() => setSelectedDeposit('1')}
              />
              <label htmlFor="deposit1">Deposit 1</label>
            </div>
            <div>
              <input
                type="radio"
                id="deposit2"
                name="deposit"
                value="2"
                checked={selectedDeposit === '2'}
                onChange={() => setSelectedDeposit('2')}
              />
              <label htmlFor="deposit2">Deposit 2</label>
            </div>
            <div>
              <input
                type="radio"
                id="deposit3"
                name="deposit"
                value="3"
                checked={selectedDeposit === '3'}
                onChange={() => setSelectedDeposit('3')}
              />
              <label htmlFor="deposit3">Deposit 3</label>
            </div>
            <LevelsChart levels={filterDataByDeposit()} deliveries = {deliveryData}/>
          </>
        ) : null}
      </div>
      <div className="error-message">{error}</div>
    </div>
  );
}
