/*
The book project lets a user keep track of different books they would like to read, are currently
reading, have read or did not finish.
Copyright (C) 2020  Karan Kumar

This program is free software: you can redistribute it and/or modify it under the terms of the
GNU General Public License as published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program.
If not, see <https://www.gnu.org/licenses/>.
*/

import { LinearProgress, makeStyles } from "@material-ui/core"
import React from 'react'

type StyleProps = {
  color: string
}

type PasswordStrengthMeterProps = {
  score?: number
}

interface PasswordMeter {
  progress: number;
  color: string;
  descriptor: string;
}

const PASSWORD_STRENGTH = "Password Strength: ";

const STRENGTH_METERS: { [score: number]: PasswordMeter } = {
  0: { progress: 20, color: '#ef2929', descriptor: PASSWORD_STRENGTH + 'Weak' },
  1: { progress: 40, color: '#f57900', descriptor: PASSWORD_STRENGTH + 'Fair' },
  2: { progress: 60, color: '#fbe209', descriptor: PASSWORD_STRENGTH + 'Good' },
  3: { progress: 80, color: '#00ff40', descriptor: PASSWORD_STRENGTH + 'Strong' },
  4: { progress: 100, color: '#008000', descriptor: PASSWORD_STRENGTH + 'Very strong' },
}

const useStyles = makeStyles({
  barColorPrimary: (props: StyleProps) => ({
    backgroundColor: props.color
  }),
});

function isPasswordBlank(score?: number): score is undefined {
  return score === undefined;
}

function calculatePasswordMeter(score?: number): PasswordMeter {
  const blankPassword: PasswordMeter = {
    color: '#ef2929',
    progress: 0,
    descriptor: 'Password is blank',
  }

  if (isPasswordBlank(score)) {
    return blankPassword
  }

  return STRENGTH_METERS[score]
}

function PasswordStrengthMeter({ score }: PasswordStrengthMeterProps): JSX.Element {
  const { color, progress, descriptor }: PasswordMeter = calculatePasswordMeter(score);
  const classes = useStyles({ color });
  return (
    <div>
      <LinearProgress
        classes={{ barColorPrimary: classes.barColorPrimary }}
        variant="determinate"
        value={progress} />
      <p style={{ textAlign: "center" }}>{descriptor}</p>
    </div>
  );
}

export default PasswordStrengthMeter;
