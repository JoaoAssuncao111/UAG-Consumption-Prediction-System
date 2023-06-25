import React, { useEffect, useState } from 'react';
import { ReadingDataInput } from './ReadingDataInput';
import { api } from '../api';
import { format } from 'date-fns';
import { Link } from 'react-router-dom';
import { LevelsChart } from './LevelsChart';

export function Levels() {
  const [location, setLocation] = useState(0);
  const [startDate, setStartDate] = useState(undefined);
  const [endDate, setEndDate] = useState(undefined);
  const [selectedDeposit, setSelectedDeposit] = useState('all');
  const [data, setData] = useState([]);

  const handleSubmit = async () => {
    if (!(startDate && endDate)) {
      console.log('error');
      return;
    }
    const formattedStartDate = format(startDate, 'yyyy-MM-dd');
    const formattedEndDate = format(endDate, 'yyyy-MM-dd');
    console.log(formattedEndDate);
    try {
      const resp = await fetch(
        `${api}/reading/levels?startDate=${formattedStartDate}&endDate=${formattedEndDate}&location=${location}`
      );

      const json = await resp.json();
      if (json) {
        setData(json);
        console.log(json);
      }
    } catch (error) {
      console.log(error);
    }
  };

  const filterDataByDeposit = () => {
    if (selectedDeposit === 'all') {
      return data;
    } else {
      const depositNumber = parseInt(selectedDeposit); // Convert selectedDeposit to integer
      return data.filter((item) => item.depositNumber === depositNumber);
    }
  };

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
        <h1>{data.length === 0 ? null : 'Gráfico de Nível/Consumo de Gás'}</h1>
        {data.length > 0 ? (
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
            <LevelsChart data={filterDataByDeposit()} />
          </>
        ) : null}
      </div>
    </div>
  );
}
