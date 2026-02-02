package net.sfelabs.knox.hilt.di

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.PolicyComponent
import net.sfelabs.knox.core.feature.api.PolicyKey
import net.sfelabs.knox.core.feature.api.PolicyState
import net.sfelabs.knox.core.feature.data.repository.CachedPolicyRegistry
import net.sfelabs.knox.core.feature.data.repository.DefaultPolicyRegistry
import net.sfelabs.knox.core.feature.domain.registry.PolicyRegistry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HiltPolicyRegistry @Inject constructor() : PolicyRegistry {
    private val delegate = CachedPolicyRegistry(DefaultPolicyRegistry())

    @Inject
    fun setComponents(components: Set<@JvmSuppressWildcards PolicyComponent<out PolicyState>>) {
        delegate.components = components
    }

    override fun <T : PolicyState> getHandler(key: PolicyKey<T>) = delegate.getHandler(key)

    override suspend fun getAllPolicies() = delegate.getAllPolicies()

    override suspend fun getPolicies(category: PolicyCategory) = delegate.getPolicies(category)

    override fun isRegistered(key: PolicyKey<*>) = delegate.isRegistered(key)

    override fun getComponent(key: PolicyKey<*>) = delegate.getComponent(key)

    override suspend fun getPolicyState(featureName: String) = delegate.getPolicyState(featureName)

    override suspend fun <T : PolicyState> setPolicyState(
        policyKey: PolicyKey<T>,
        state: T
    ): ApiResult<Unit> = delegate.setPolicyState(policyKey, state)

    // Capability-based query delegations
    override fun getByCapability(capability: PolicyCapability) = delegate.getByCapability(capability)
    override fun getByCapabilities(capabilities: Set<PolicyCapability>, matchAll: Boolean) =
        delegate.getByCapabilities(capabilities, matchAll)
    override fun getByCategory(category: PolicyCategory) = delegate.getByCategory(category)
    override fun query(
        category: PolicyCategory?,
        capabilities: Set<PolicyCapability>?,
        matchAllCapabilities: Boolean
    ) = delegate.query(category, capabilities, matchAllCapabilities)
    override fun getAllComponents() = delegate.getAllComponents()
}
