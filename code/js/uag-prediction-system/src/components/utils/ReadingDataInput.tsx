import React, { useState, useEffect } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { api } from '../../api';
import Select from 'react-select';

export function ReadingDataInput({ location, setLocation, startDate, setStartDate, endDate, setEndDate, isFetchButtonDisabled, setIsFetchButtonDisabled}) {
  const [error, setError] = useState('');
  const [locations, setLocations] = useState([]);
  const [selectedOption, setSelectedOption] = useState(null);
  

  useEffect(() => {
    fetchLocations();
  }, []);

  const fetchLocations = async () => {
    try {
      const resp = await fetch(`${api}/uags`);
      const data = await resp.json();

      if (data) {
        setLocations(data);
        console.log(data);
      }
    } catch (error) {
      console.log(error);
    }
  };

  const handleDateChange = (date, isStartDate) => {
    if (isStartDate) {
      setStartDate(date);
      if (endDate && date > endDate) {
        setError('Janela de tempo inválida');
        setIsFetchButtonDisabled(true)
        
      } else {
        setIsFetchButtonDisabled(false)
        setError('');
      }
    } else {
      setEndDate(date);
      if (startDate && date < startDate) {
        setError('Janela de tempo inválida');
        setIsFetchButtonDisabled(true)
        
      } else {
        setIsFetchButtonDisabled(false)
        setError('');
      }
    }
  };

  const handleLocationChange = (selectedOption) => {
    setSelectedOption(selectedOption);
    setLocation(selectedOption?.value);
  };

  const options = locations.map((location) => ({
    value: location.id,
    label: location.name,
  }));

  return (
    <div>
    
          <Select className='select-bar'
            value={selectedOption}
            onChange={handleLocationChange}
            options={options}
            placeholder="Selecione uma localização"
            isSearchable
          />
      
          <DatePicker className="date-picker" selected={startDate} onChange={(date) => handleDateChange(date, true)} dateFormat="yyyy-MM-dd" placeholderText="Data Inicial"/>
        
          <DatePicker  className="date-picker" selected={endDate} onChange={(date) => handleDateChange(date, false)} dateFormat="yyyy-MM-dd" placeholderText="Data Final"/>
        
        {error && <p className="error-message">{error}</p>}
      
    </div>
  );
}
