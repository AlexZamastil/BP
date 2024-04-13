import { createTheme } from '@mui/material/styles';

/**
 * Author: Alex Zamastil
 * This file defines a theme used for a project based on MUI library.
 */

const theme = createTheme({
  palette: {
    primary: {
      main: '#61e248',
    },
    secondary: {
      main: '#ff0000',
    },
    dark: {
      main: '#D9DDDC',
    },
    black: {
      main: '#000',
    }
  
  },
});

export default theme;