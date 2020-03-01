package com.mctech.showcase.feature.flicker_data.tag_history

import com.mctech.showcase.feature.flicker_domain.service.FlickerTagHistoryService

/**
 * @author MAYCON CARDOSO on 2020-02-28.
 */
class FlickerTagHistoryRepository(
    private val dataSource: FlickerTagHistoryDataSource
) : FlickerTagHistoryService by dataSource