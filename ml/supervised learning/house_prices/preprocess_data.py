import pandas as pd
from sklearn.preprocessing import MinMaxScaler, Binarizer


def scale_data(houses_df: pd.DataFrame):
    scaler = MinMaxScaler(feature_range=(0, 1))
    return scaler.fit_transform(houses_df[['LotArea', 'YrSold']])


def binarize_data(houses_df: pd.DataFrame):
    prior_to_09_X = houses_df[['YrSold']]
    lot_area_under_11k_X = houses_df[['LotArea']]
    binarizer = Binarizer(threshold=2006).fit(prior_to_09_X)
    houses_df[['YrSold']] = binarizer.transform(prior_to_09_X)
    binarizer = Binarizer(threshold=11000).fit(lot_area_under_11k_X)
    houses_df[['LotArea']] = binarizer.transform(lot_area_under_11k_X)
    return houses_df
