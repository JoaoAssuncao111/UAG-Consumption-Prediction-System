import React, { useState } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

export function ReadingDataInput({ location, setLocation, startDate, setStartDate, endDate, setEndDate }) {
  const [error, setError] = useState('');

  const handleDateChange = (date, isStartDate) => {
    if (isStartDate) {
      setStartDate(date);
      if (endDate && date > endDate) {
        setError('Janela de tempo inválida');
      } else {
        setError('');
      }
    } else {
      setEndDate(date);
      if (startDate && date < startDate) {
        setError('Janela de tempo inválida');
      } else {
        setError('');
      }
    }
  };

  return (
    <div>
      <div>
        <div>
          <label>Id localização</label>
          <input placeholder="ID localização" value={location} onChange={(event) => setLocation(event.target.value)} />
        </div>
        <div>
          <label>Data Inicial</label>
          <DatePicker selected={startDate} onChange={(date) => handleDateChange(date, true)} dateFormat="yyyy-MM-dd" />
          <label>Data Final</label>
          <DatePicker selected={endDate} onChange={(date) => handleDateChange(date, false)} dateFormat="yyyy-MM-dd" />
        </div>
        {error && <p className="error">{error}</p>}
      </div>
    </div>
  );
}
