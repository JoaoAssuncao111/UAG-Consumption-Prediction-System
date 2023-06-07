import React, { useState } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

export function ReadingDataInput({ location, setLocation, startDate, setStartDate, endDate, setEndDate }) {
  return (
    <div>
      <div>
        <div>
          <label>Id localização</label>
          <input placeholder="ID localização" value={location} onChange={(event) => setLocation(event.target.value)} />
        </div>
        <div>
          <label>Data Inicial</label>
          <DatePicker selected={startDate} onChange={(date) => setStartDate(date)} dateFormat="yyyy-MM-dd" />
          <label>Data Final</label>
          <DatePicker selected={endDate} onChange={(date) => setEndDate(date)} dateFormat="yyyy-MM-dd" />
        </div>
      </div>
    </div>
  );
}



