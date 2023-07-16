import React from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

export function DatePickerInput({ selectedDate, handleDateChange, placeholderText }) {
  return (
    <DatePicker
      className="date-picker"
      selected={selectedDate}
      onChange={handleDateChange}
      dateFormat="yyyy-MM-dd"
      placeholderText={placeholderText}
    />
  );
}