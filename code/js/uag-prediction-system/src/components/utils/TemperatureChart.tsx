import React from 'react';
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,

} from 'recharts';
 const lineStrokeWidth = 4
export function TemperatureChart({ data }) {

  
  
  return (
    <div style={{ width: '100%' }}>
      <ResponsiveContainer width="100%" height={400}>
        <LineChart data={data} margin={{ left: 20, right: 20 }}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="dateHour" tickLine={null} />
          <YAxis />
          <Tooltip />
          <Legend />
          <Line
            type="monotone"
            dataKey="minValue"
            stroke="#0000FF"
            strokeWidth={lineStrokeWidth}
            fill="#00b0f3"
            name="Min Temperature"
            dot={false}
          />
          <Line
            type="monotone"
            dataKey="maxValue"
            stroke="#FF0000"
            strokeWidth={lineStrokeWidth}
            fill="#ff5d53"
            name="Max Temperature"
            dot={false}
          />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
}

export default TemperatureChart;
