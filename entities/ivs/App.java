/*
 *     Copyright (C) 2016 Mobile Interactive Systems Research Group
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package at.fhooe.studymate.entities.ivs;

import com.google.gson.annotations.SerializedName;

/**
 * App in experiment, is defined by the package name
 */
public class App extends IndependentVariable{
  @SerializedName("package")
  private String packageName;

  public App() {
    //Needed for gson
  }

 /**
  * @return package name
  */
  public String getPackageName() {
    return packageName;
  }

}
