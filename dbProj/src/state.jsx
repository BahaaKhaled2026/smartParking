import { atom } from 'recoil';

export const currPanel = atom({
  key: 'currPanel',
  default: 2,
});
export const chosenLot = atom({
    key: 'chosenLot',
    default: null,
  });

