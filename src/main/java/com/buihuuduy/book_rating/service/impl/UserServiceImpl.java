package com.buihuuduy.book_rating.service.impl;

import com.buihuuduy.book_rating.DTO.request.UserEntityRequest;
import com.buihuuduy.book_rating.DTO.request.UserInfoRequest;
import com.buihuuduy.book_rating.DTO.response.AccountResponse;
import com.buihuuduy.book_rating.DTO.response.UserDetailResponse;
import com.buihuuduy.book_rating.DTO.response.UserInfoResponse;
import com.buihuuduy.book_rating.entity.CommentEntity;
import com.buihuuduy.book_rating.entity.FavoriteBookEntity;
import com.buihuuduy.book_rating.entity.FollowingAccountEntity;
import com.buihuuduy.book_rating.entity.UserEntity;
import com.buihuuduy.book_rating.exception.CustomException;
import com.buihuuduy.book_rating.exception.ErrorCode;
import com.buihuuduy.book_rating.mapper.UserMapper;
import com.buihuuduy.book_rating.repository.*;
import com.buihuuduy.book_rating.service.UserService;
import com.buihuuduy.book_rating.service.utils.CommonFunction;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService
{
    private final FollowingAccountRepository followingAccountRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final FavoriteBookRepository favoriteBookRepository;
    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, FollowingAccountRepository followingAccountRepository, FavoriteBookRepository favoriteBookRepository, BookRepository bookRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.followingAccountRepository = followingAccountRepository;
        this.favoriteBookRepository = favoriteBookRepository;
        this.bookRepository = bookRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public Integer getIdByToken(String token) {
        String username = CommonFunction.getUsernameFromToken(token);
        UserEntity user = userRepository.findByUsername(username);
        return user.getId();
    }

    @Override
    public void updateUser(Integer userId, UserInfoRequest userInfoRequest)
    {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUser(userEntity, userInfoRequest);
        userRepository.save(userEntity);
    }

    @Override
    public UserInfoResponse getUserInfoByToken(String token) {
        String username = CommonFunction.getUsernameFromToken(token);
        UserEntity userEntity = userRepository.findByUsername(username);
        UserInfoResponse userInfoResponse = userMapper.toUserInfo(userEntity);
        userInfoResponse.setUsername(userEntity.getUsername());
        return userInfoResponse;
    }

    @Override
    public List<AccountResponse> getFollowingAccountByUser(String token, Integer userId)
    {
        List<Integer> listFollowingAccountId = followingAccountRepository.findAllFollowingAccountIdsByYourAccountId(userId);
        List<AccountResponse> accountResponseList = new ArrayList<>();
        for(Integer followingAccountId : listFollowingAccountId)
        {
            AccountResponse accountResponse = new AccountResponse();
            UserEntity userEntity = userRepository.findById(followingAccountId).orElseThrow();

            accountResponse.setUserId(userEntity.getId());
            accountResponse.setUserName(userEntity.getUsername());
            accountResponse.setUserImage(userEntity.getUserImage());
            accountResponse.setFullName(userEntity.getFullName());

            String username = CommonFunction.getUsernameFromToken(token);
            UserEntity myAccount = userRepository.findByUsername(username);

            accountResponseList.add(accountResponse);
            for (AccountResponse user : accountResponseList){
                if(introspectFollowBack(user.getUserId(), myAccount.getId()) == 0){
                    user.setFollowBack(false);
                }else {
                    user.setFollowBack(true);
                }
            }
        }
        return accountResponseList;
    }

    @Override
    public List<AccountResponse> getFollowerAccountByUser(String token, Integer userId)
    {
        List<Integer> listFollowerAccountId = followingAccountRepository.findAllFollowerAccountIdsByFollowingAccountId(userId);
        List<AccountResponse> accountResponseList = new ArrayList<>();
        for(Integer followingAccountId : listFollowerAccountId)
        {
            AccountResponse accountResponse = new AccountResponse();
            UserEntity userEntity = userRepository.findById(followingAccountId).orElseThrow();

            accountResponse.setUserId(userEntity.getId());
            accountResponse.setUserName(userEntity.getUsername());
            accountResponse.setUserImage(userEntity.getUserImage());

            accountResponseList.add(accountResponse);

            String username = CommonFunction.getUsernameFromToken(token);
            UserEntity myAccount = userRepository.findByUsername(username);

            for (AccountResponse user : accountResponseList){
                if(introspectFollowBack(user.getUserId(), myAccount.getId()) == 0){
                    user.setFollowBack(false);
                }else {
                    user.setFollowBack(true);
                }
            }
        }
        return accountResponseList;
    }

    @Override
    // Xem thong tin ca nhan cua nguoi dung nao do
    public UserDetailResponse getUserDetailInfo(String token, Integer userId)
    {
        UserDetailResponse userDetailResponse = new UserDetailResponse();
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        String username = CommonFunction.getUsernameFromToken(token);
        UserEntity myAccount = userRepository.findByUsername(username);

        userDetailResponse.setIsFollowing(followingAccountRepository.checkFollowStatus(myAccount.getId(), userEntity.getId()) > 0);
        userDetailResponse.setId(userEntity.getId());
        userDetailResponse.setUserName(userEntity.getUsername());
        userDetailResponse.setUserImage(userEntity.getUserImage());
        userDetailResponse.setBookNumberPost(bookRepository.countBookByUsername(userEntity.getUsername()));
        userDetailResponse.setFollowingAccounts(getFollowingAccountByUser(token, userId).size());
        userDetailResponse.setFollowerAccounts(getFollowerAccountByUser(token, userId).size());
        return userDetailResponse;
    }

    // CHECK frontend send id or token
    @Override
    public void followUser(String token, Integer followedId)
    {
        // Get current user
        String currentUsername = CommonFunction.getUsernameFromToken(token);
        UserEntity currentUser = userRepository.findByUsername(currentUsername);
        if(currentUser == null) throw new CustomException(ErrorCode.USER_NOT_FOUND);

        FollowingAccountEntity followingAccount = followingAccountRepository
                .findByFollowerAccountIdAndFollowedAccountId(currentUser.getId(), followedId);
        if(followingAccount != null){
            followingAccount.setIsActive(true);
            followingAccountRepository.save(followingAccount);
        } else{
            FollowingAccountEntity newFollowingAccountEntity = new FollowingAccountEntity();
            // Nguoi dang di follow - nguoi duoc follow
            newFollowingAccountEntity.setFollowerAccountId(currentUser.getId());
            newFollowingAccountEntity.setFollowedAccountId(followedId);
            followingAccountRepository.save(newFollowingAccountEntity);
        }
    }

    @Override
    public void unfollowUser(String token, Integer unFollowedId)
    {
        // Get current user
        String currentUsername = CommonFunction.getUsernameFromToken(token);
        UserEntity currentUser = userRepository.findByUsername(currentUsername);
        if(currentUser == null) throw new CustomException(ErrorCode.USER_NOT_FOUND);

        FollowingAccountEntity followingAccount = followingAccountRepository
                .findByFollowerAccountIdAndFollowedAccountId(currentUser.getId(), unFollowedId);
        followingAccount.setIsActive(false);
        followingAccountRepository.save(followingAccount);
    }

    @Override
    public void markAsFavorite(String token, Integer bookId)
    {
        FavoriteBookEntity favoriteBookEntity = new FavoriteBookEntity();
        favoriteBookEntity.setBookId(bookId);
        String currentUsername = CommonFunction.getUsernameFromToken(token);
        UserEntity currentUser = userRepository.findByUsername(currentUsername);
        if(currentUser == null) throw new CustomException(ErrorCode.USER_NOT_FOUND);
        favoriteBookEntity.setUserId(currentUser.getId());
        favoriteBookRepository.save(favoriteBookEntity);
    }

    @Override
    public void unMarkFavorites(String token, Integer bookId) {
        String currentUsername = CommonFunction.getUsernameFromToken(token);
        UserEntity currentUser = userRepository.findByUsername(currentUsername);
        FavoriteBookEntity favoriteBookEntity = favoriteBookRepository.findByUserIdAndBookId(currentUser.getId(), bookId);
        favoriteBookRepository.delete(favoriteBookEntity);
    }

    @Override
    public List<AccountResponse> getFollowingAccountByToken(String token) {
        String username = CommonFunction.getUsernameFromToken(token);
        UserEntity userEntity = userRepository.findByUsername(username);
        if(userEntity == null) throw new CustomException(ErrorCode.USER_NOT_FOUND);
        return getFollowingAccountByUser(token, userEntity.getId());
    }

    @Override
    public List<AccountResponse> getFollowerAccountByToken(String token) {
        String username = CommonFunction.getUsernameFromToken(token);
        UserEntity userEntity = userRepository.findByUsername(username);
        if(userEntity == null) throw new CustomException(ErrorCode.USER_NOT_FOUND);
        return getFollowerAccountByUser(token, userEntity.getId());
    }

    @Override
    public UserDetailResponse getUserDetailInfoByToken(String token) {
        String username = CommonFunction.getUsernameFromToken(token);
        UserEntity userEntity = userRepository.findByUsername(username);
        if(userEntity == null) throw new CustomException(ErrorCode.USER_NOT_FOUND);

        UserDetailResponse userDetailResponse = new UserDetailResponse();
        userDetailResponse.setId(userEntity.getId());
        userDetailResponse.setUserName(userEntity.getUsername());
        userDetailResponse.setUserImage(userEntity.getUserImage());
        userDetailResponse.setBookNumberPost(bookRepository.countBookByUsername(username));
        userDetailResponse.setFollowingAccounts(getFollowingAccountByUser(token, userEntity.getId()).size());
        userDetailResponse.setFollowerAccounts(getFollowerAccountByUser(token, userEntity.getId()).size());
        return userDetailResponse;
    }

    @Override
    public List<UserEntity> getAllUser() {
        return userRepository.findAllByIsActiveIsTrueAndIsAdminIsFalse();
    }

    @Override
    public void deleteUser(Integer userId) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        List<FavoriteBookEntity> favoriteBookEntity = favoriteBookRepository.findAllByUserId(userEntity.getId());
        favoriteBookRepository.deleteAll(favoriteBookEntity);
        List<FollowingAccountEntity> followingAccountList = followingAccountRepository.findByUserId(userEntity.getId());
        followingAccountRepository.deleteAll(followingAccountList);
        List<CommentEntity> commentList = commentRepository.findByUserId(userEntity.getId());
        commentRepository.deleteAll(commentList);
        userEntity.setIsActive(false);
        userRepository.save(userEntity);
    }

    @Override
    public UserInfoResponse getUserInfoById(Integer userId) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        UserInfoResponse userInfoResponse = userMapper.toUserInfo(userEntity);
        userInfoResponse.setUsername(userEntity.getUsername());
        return userInfoResponse;
    }

    public Long introspectFollowBack(Integer followingAccountId, Integer followerAccountId){
        return userRepository.introspectFollowBack(followingAccountId, followerAccountId);
    }
}
