package com.users.list.ui

import com.users.list.ui.displayable.UserDisplayable

interface ListContract {
  interface View {
    fun displayUserList(users: List<UserDisplayable>)
    fun updateUserListItem(userName: String, repositories: String)
  }

  interface Presenter {
    fun fetchUsers()
    fun fetchUsersRepositories(userName: String)
    fun filterUsers(searchQuery: String?)
    fun releaseResources()
  }
}