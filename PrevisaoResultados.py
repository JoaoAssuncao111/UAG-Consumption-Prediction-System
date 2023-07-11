#!/usr/bin/env python
# coding: utf-8

# In[87]:


from sklearn import neural_network
import numpy as np 
import pandas as pd
import matplotlib.pyplot as plt
#import tensorflow as tf
from sklearn.linear_model import LinearRegression
from sklearn.metrics import mean_absolute_error , mean_squared_error 
import warnings
import pickle
from datetime import datetime, timedelta

def MediaMovel(df, tmin, tmax):
    MA= []
    for i in range(len(df)):
        if i >= abs(tmin):
            media = df.iloc[i+tmin:i+tmax+1]['Consumo'].mean()
        else:
            media = None
        MA.append(media)
        
    return MA

def DayType(df):
    lista = [0] * len(df)
    feriados= ["2023-01-01", "2023-04-25", "2023-05-01", "2023-06-10", "2023-08-15", "2023-10-05", "2023-11-01", "2023-12-01", "2023-12-08", "2023-12-25"]
    k = 0
    for i in range(df.index[0],df.index[-1]):
        data_str = df.loc[i, 'Data'].strftime("%Y-%m-%d")
        data = datetime.strptime(data_str, "%Y-%m-%d")
        for j in feriados:
            feriado = datetime.strptime(j, "%Y-%m-%d")
            if data.month == feriado.month and data.day == feriado.day:
                lista[k] = 1; break
                
        if data.weekday() == 5 or data.weekday() == 6:
            lista[k] = 1
        k+=1
        
    return lista



size=9+5
df = pd.DataFrame({'Data': [],'Consumo': [],'MA_3a5': [],'MA_3a9': [],'DayType': [],'Tmin': [], 'Tmax': []})


# In[111]:


for i in range(size):
    df.at[i, 'Tmin'] = None
    df.at[i, 'Tmax'] = None 
    df.at[i, 'Consumo'] = None
    df.at[i, 'MA_3a5'] = None
    df.at[i, 'MA_3a9'] = None
    df.at[i, 'Data'] =  None
    df.at[i, 'DayType'] = DayType(df)[i]
df


# In[164]:


#consumo_old=np.array([-1,-2,-3,-4,-5,-6,-7,-8,-9.0])

consumo_old=np.array([-10,-11,-12,-10,-11,-12,-10,-11,-12.0]) # Substituir pelo vetor correto


# In[165]:


for i in range(9):
   df.at[i,'Consumo']=consumo_old[i] #Substituir pelo vetor correto
for i in range(9,len(df)):
    df.at[i,'Tmin']=df_temp.at[i,'Tmin'] #Substituir pelo vetor correto
    df.at[i,'Tmax']=df_temp.at[i,'Tmax'] #Substituir pelo vetor correto
df


# In[60]:


coefs=np.array([0.19049154, 1.57162265, 0.02758893, 0.52786252])
print(np.shape(coefs))
intercept=-20.60712828239192


# In[167]:


for i in range(9,9+7): #substituir pelo frame futuro correto  
    df.at[i,'MA_3a9']=MediaMovel(df,-9,-3)[i]
    df.at[i,'Consumo']=intercept+np.dot(coefs,df.iloc[i,3:])
df


# In[169]:


previsão=df['Data','Consumo'][9:]
previsão


# In[ ]:




