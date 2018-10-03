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

package at.fhooe.studymate.interfaces;

import at.fhooe.studymate.entities.ExperimentInfo;
import at.fhooe.studymate.entities.ParticipantInfo;

/**
 * Interface to load and cache the experimental design and participant information
 */
public interface IDataManager {
  /**
   * Persist experimental design
   *
   * @param experimentJson experimental design formatted as JSON
   */
  void cacheExperiment(String experimentJson);

  /**
   * Persist information about current participant
   *
   * @param participantInfo about current participant
   */
  void cacheParticipant(ParticipantInfo participantInfo);

  /**
   * Persist base url string
   *
   * @param baseUrl of StudyMate Web
   */
  void cacheBaseUrl(String baseUrl);

  /**
   * @return experimental design
   */
  ExperimentInfo getExperiment();

  /**
   * @return information about current participant
   */
  ParticipantInfo getParticipant();

  /**
   * @return base URL of StudyMate Web
   */
  String getBaseUrl();

  void cacheFieldRep(String fieldRep);

  String getFieldRep();
}
