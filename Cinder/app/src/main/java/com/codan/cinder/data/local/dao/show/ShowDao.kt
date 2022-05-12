package com.codan.cinder.data.local.dao.show

import com.codan.cinder.data.local.dao.PagedCrudDao
import com.codan.cinder.data.local.domain.show.Show

interface ShowDao<T : Show> : PagedCrudDao<Long, T> {
}