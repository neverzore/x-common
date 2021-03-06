/*
 * Copyright (c) 2020 neverzore (https://github.com/neverzore).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.neverzore.common.domain.service;

import tech.neverzore.common.util.response.Response;

import java.util.Collection;

/**
 * @author: zhouzb
 * @date: 2019/7/19
 */
public interface IBaseService<E, T> {
    /**
     * 生成领域对象
     *
     * @return  领域对象
     */
    default E entityInstance() {
        throw new IllegalAccessError("please implement this method in subclass");
    }

    /**
     * 生成领域对象
     * @param id    主键
     * @return  领域对象
     */
    default E entityInstance(T id) {
        throw new IllegalAccessError("please implement this method in subclass");
    }

    /**
     * 通过主键获取对象
     *
     * @param id 主键
     * @return  对象响应
     */
    Response<E> get(T id);

    /**
     * 通过主键集合获取对象集合
     *
     * @param ids 主键集合
     * @return  对象集合响应
     */
    Response<Collection<E>> get(Collection<T> ids);

    /**
     * 插入对象
     *
     * @param entity 实体对象
     * @return  插入对象响应
     */
    Response<E> insert(E entity);

    /**
     * 批量插入对象
     *
     * @param entities 对象集合
     * @return  批量插入对象响应
     */
    Response<Collection<E>> insert(Collection<E> entities);

    /**
     * 更新对象
     *
     * @param entity 实体对象
     * @return  更新对象响应
     */
    Response<E> update(E entity);

    /**
     * 更新对象集合
     *
     * @param entities 对象集合
     * @return  更新对象集合响应
     */
    Response<Collection<E>> update(Collection<E> entities);

    /**
     * 删除对象
     *
     * @param id 对象主键
     * @return  删除对象响应
     */
    Response<Void> delete(T id);

    /**
     * 删除对象集合
     *
     * @param ids 对象主键集合
     * @return  删除对象集合响应
     */
    Response<Void> delete(Collection<T> ids);

    /**
     * 获取对象列表
     *
     * @return  对象列表响应
     */
    Response<Collection<E>> list();
}
